package io.github.sokrato.mw;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class DefaultContextTest {
    private Pipeline<Integer, Integer> pipeline;
    private DefaultContext<Integer, Integer> context;
    private Handler<Integer, Integer> handler;

    private static final String n = "name";
    private static final String k = "k";
    private static final String v = "v";

    @BeforeMethod
    public void setup() {
        pipeline = mock(Pipeline.class);
        handler = mock(Handler.class);
        context = new DefaultContext<>(n, handler, pipeline);
    }

    @Test
    public void testFields() {
        assertEquals(context.name(), n);
        assertEquals(context.handler(), handler);
        assertEquals(context.pipeline(), pipeline);
    }

    @Test
    public void testAttributeMap() {
        when(pipeline.getAttr(k)).thenReturn(null).thenReturn(v);
        when(pipeline.setAttr(k, v)).thenReturn(null);
        when(pipeline.delAttr(k)).thenReturn(null);

        assertNull(context.getAttr(k));
        assertEquals(context.getAttr(k), v);
        verify(pipeline, times(2)).getAttr(k);

        assertNull(context.setAttr(k, v));
        verify(pipeline, times(1)).setAttr(k, v);

        assertNull(context.delAttr(k));
        verify(pipeline, times(1)).delAttr(k);
    }

    @Test
    public void testProceed() {
        DefaultContext<Integer, Integer> next = mock(DefaultContext.class);
        Handler<Integer, Integer> h = mock(Handler.class);
        when(next.handler()).thenReturn(h);
        when(h.handle(1, next)).thenReturn(2);

        context.setNext(next);
        assertEquals(context.proceed(1).intValue(), 2);
        verify(next, times(1)).handler();
        verify(h, times(1)).handle(1, next);
    }
}