package io.github.sokrato.mw;

import io.github.sokrato.mw.util.Identity;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DefaultEngineTest {

    @Test
    public void example() {
        LoggingMW<?, ?> logger = new LoggingMW<>();

        Handler<Integer, Integer> inc = (i, c) -> {
            int amount = c.getAttr("add-by", 1);
            return c.proceed(i + amount);
        };
        Handler<Integer, Integer> x2 = (i, c) -> c.proceed(i * 2);
        Identity<Integer> self = new Identity<>();

        Engine<Integer, Integer> one = new DefaultEngine<>(pipeline -> {
            pipeline.addLast("log1", (Handler<Integer, Integer>) logger);
            pipeline.addLast("increment", inc);
            pipeline.addLast("double", x2);
            pipeline.addLast("identity", self);
            return pipeline;
        });
        assertEquals(one.process(1).intValue(), 4);
        assertEquals(one.process(2).intValue(), 6);
        assertTrue(logger.reqList.size() == 2
                && Integer.valueOf(1).equals(logger.reqList.get(0))
                && Integer.valueOf(2).equals(logger.reqList.get(1)));

        assertTrue(logger.resList.size() == 2
                && Integer.valueOf(4).equals(logger.resList.get(0))
                && Integer.valueOf(6).equals(logger.resList.get(1)));

        Engine<Integer, Integer> two = new DefaultEngine<>(pipeline -> {
            pipeline.addLast("double", x2);
            pipeline.addLast("increment", inc);
            pipeline.addLast("identity", self);
            return pipeline;
        });
        assertEquals(two.process(1).intValue(), 3);
        assertEquals(two.process(2).intValue(), 5);
    }

    @Test
    public void testProcessMock() {
        Pipeline<Integer, Integer> pipeline = mock(Pipeline.class);
        when(pipeline.process(any())).thenReturn(1);

        PipelineInitializer<Integer, Integer> init = mock(PipelineInitializer.class);
        when(init.init(any())).thenReturn(pipeline);
        DefaultEngine<Integer, Integer> engine = new DefaultEngine<>(init);
        assertEquals(engine.process(0).intValue(), 1);
        verify(init, times(1)).init(any());
        verify(pipeline, times(1)).process(0);
    }
}