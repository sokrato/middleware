package io.github.sokrato.mw;

/**
 * Handler execution context.
 *
 * @param <Q> request type
 * @param <R> response type
 */
public interface Context<Q, R> extends AttributeMap {
    /**
     * @return name of current context
     */
    String name();

    /**
     * get the handler field
     *
     * @return handler
     */
    Handler<Q, R> handler();

    /**
     * process a request
     *
     * @param req request
     * @return result of processing
     */
    R proceed(Q req);

    /**
     * @return the {@link Pipeline} this context is attached to
     */
    Pipeline<Q, R> pipeline();
}
