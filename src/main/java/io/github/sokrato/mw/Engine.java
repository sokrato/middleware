package io.github.sokrato.mw;

public interface Engine<Q, R> {
    R process(Q req);
}
