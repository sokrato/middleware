package io.github.sokrato.mw.util;

import io.github.sokrato.mw.Context;
import io.github.sokrato.mw.Handler;

/**
 * This handler does nothing but returns the request object as is.
 * Usually used at the end of pipeline as a dummy handler.
 *
 * @param <T> type of request and response
 */
public class Identity<T> implements Handler<T, T> {

    /**
     * Simply returns the request object.
     *
     * @param req     request
     * @param context context
     * @return the request object
     */
    @Override
    public T handle(T req, Context<? super T, ? extends T> context) {
        return req;
    }
}
