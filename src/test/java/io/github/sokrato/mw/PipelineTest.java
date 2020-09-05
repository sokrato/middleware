package io.github.sokrato.mw;

import io.github.sokrato.mw.util.Identity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class PipelineTest {
    private Pipeline<Integer, Integer> pipeline;
    private final Handler<Integer, Integer> handler = new LoggingMW<>();

    @BeforeMethod
    public void init() {
        pipeline = new Pipeline<>();
    }

    @Test
    public void testProcess() {
        try {
            pipeline.process(0);
            fail("pipeline.process should fail since there is no handlers attached");
        } catch (NoMoreHandlerException ignored) {
        }
    }

    @Test
    public void testAddRemove() {
        String name = "one";
        ensureEmpty(name, handler);

        // now its not empty
        pipeline.addFirst(name, handler);
        assertEquals(pipeline.getFirst().handler(), handler);
        assertEquals(pipeline.getLast().handler(), handler);
        assertEquals(pipeline.get(name).handler(), handler);
        assertEquals(pipeline.get(handler).handler(), handler);

        assertEquals(pipeline.remove(name).handler(), handler);
        ensureEmpty(name, handler);

        pipeline.addLast(name, handler);
        assertEquals(pipeline.remove(handler).handler(), handler);
        ensureEmpty(name, handler);
    }

    private void ensureEmpty(String name, Handler<Integer, Integer> handler) {
        assertNull(pipeline.getFirst());
        assertNull(pipeline.getLast());
        assertNull(pipeline.get(name));
        assertNull(pipeline.get(handler));
    }

    @Test
    public void testThrowWhenNameNotFound() {
        String name = "name";
        try {
            pipeline.addAfter(name, name, handler);
            fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            pipeline.addBefore(name, name, handler);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testContainerOrdering() {
        pipeline.addFirst("1", handler)
                .addFirst("2", handler)
                .addLast("3", handler)
                .addAfter("3", "4", handler)
                .addBefore("1", "5", handler)
                .addAfter("2", "7", handler)
                .addLast("", new Identity<>())
                .remove("7");
        pipeline.process(1);
        String order = String.join("", ((LoggingMW<Integer, Integer>) handler).names);
        assertEquals(order, "25134");
    }

    @Test
    public void testAttributeMap() {
        testAttributeMap(pipeline);

        Map<String, Object> map = new HashMap<>();
        map.put("k", "v");
        pipeline = new Pipeline<>(map);
        assertEquals(pipeline.getAttr("k"), "v");
    }

    static void testAttributeMap(AttributeMap map) {
        String key = "key";
        assertNull(map.getAttr(key));

        String val = "default";
        assertEquals(val, map.getAttr(key, val));

        assertNull(map.setAttr(key, val));
        assertEquals(val, map.getAttr(key));

        assertEquals(map.setAttr(key, val), val);
        assertEquals(val, map.delAttr(key));
        assertNull(map.getAttr(key));
    }
}