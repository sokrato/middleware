package io.github.sokrato.mw;

import javax.annotation.Nonnull;

/**
 * An object that works as a container for {@link Handler} objects.
 *
 * @param <Q> request type
 * @param <R> response type
 */
public interface Container<Q, R> {
    default Container<Q, R> addFirst(@Nonnull Handler<Q, R> handler) {
        return addFirst(handler.toString(), handler);
    }

    Container<Q, R> addFirst(String name,
                             @Nonnull Handler<Q, R> handler);

    default Container<Q, R> addLast(@Nonnull Handler<Q, R> handler) {
        return addLast(handler.toString(), handler);
    }

    Container<Q, R> addLast(String name,
                            @Nonnull Handler<Q, R> handler);

    default Container<Q, R> addAfter(@Nonnull String afterName,
                                     @Nonnull Handler<Q, R> handler) {
        return addAfter(afterName, handler.toString(), handler);
    }

    Container<Q, R> addAfter(@Nonnull String afterName,
                             String name,
                             @Nonnull Handler<Q, R> handler);

    default Container<Q, R> addBefore(@Nonnull String beforeName,
                                      @Nonnull Handler<Q, R> handler) {
        return addBefore(beforeName, handler.toString(), handler);
    }

    Container<Q, R> addBefore(@Nonnull String beforeName,
                              String name,
                              @Nonnull Handler<Q, R> handler);

    Context<Q, R> getFirst();

    Context<Q, R> getLast();

    Context<Q, R> get(@Nonnull String name);

    Context<Q, R> get(@Nonnull Handler<Q, R> handler);

    Context<Q, R> remove(@Nonnull String name);

    Context<Q, R> remove(@Nonnull Handler<Q, R> handler);
}
