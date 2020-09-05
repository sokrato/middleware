package io.github.sokrato.mw;

public class NoMoreHandlerException extends IllegalStateException {
    public NoMoreHandlerException() {
        super("no more handler pending");
    }
}
