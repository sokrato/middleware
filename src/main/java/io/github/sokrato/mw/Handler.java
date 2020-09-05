package io.github.sokrato.mw;

/**
 * An object that {@link #handle(Object, Context)}s request according to
 * a context.
 *
 * @param <Q> request type
 * @param <R> response type
 */
@FunctionalInterface
public interface Handler<Q, R> {
    /**
     * handles a request and context
     *
     * @param req     request
     * @param context context
     * @return a result
     */
    R handle(Q req, Context<? super Q, ? extends R> context);
}
