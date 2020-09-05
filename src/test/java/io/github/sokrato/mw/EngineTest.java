package io.github.sokrato.mw;

import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class EngineTest {
    @Test
    public void testAPI() {
        Engine<String, String> engine = mock(Engine.class);
        engine.process("whatever");
    }
}