package io.github.sokrato.mw;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Pipeline<Q, R> implements Container<Q, R>, AttributeMap {
    private final DefaultContext<Q, R> head;
    private final DefaultContext<Q, R> tail;
    private final Map<String, Object> attrs;

    public Pipeline() {
        this(new HashMap<>());
    }

    public Pipeline(Map<String, Object> attrs) {
        this.attrs = attrs;
        head = new HeadContext<>();
        tail = new TailContext<>();
        head.setNext(tail);
        tail.setPrev(head);
    }

    /**
     * process request from the start of context chain
     *
     * @param req request
     * @return result of processing
     */
    public R process(Q req) {
        return head.proceed(req);
    }

    @Override
    public <T> T getAttr(String key) {
        return (T) attrs.get(key);
    }

    @Override
    public <T> T getAttr(String key, T defaultVal) {
        return (T) attrs.getOrDefault(key, defaultVal);
    }

    @Override
    public <T> T setAttr(String key, Object obj) {
        return (T) attrs.put(key, obj);
    }

    @Override
    public <T> T delAttr(String key) {
        return (T) attrs.remove(key);
    }

    private DefaultContext<Q, R> newMiddleware(String name, Handler<Q, R> handler) {
        if (handler == null)
            throw new IllegalArgumentException("handler should not be null");
        return new DefaultContext<>(name, handler, this);
    }

    @Override
    public Container<Q, R> addFirst(String name, @Nonnull Handler<Q, R> handler) {
        DefaultContext<Q, R> mw = newMiddleware(name, handler);
        return addAfter(head, mw);
    }

    @Override
    public Container<Q, R> addLast(String name, @Nonnull Handler<Q, R> handler) {
        DefaultContext<Q, R> mw = newMiddleware(name, handler);
        return addBefore(tail, mw);
    }

    private Container<Q, R> addBefore(DefaultContext<Q, R> base, DefaultContext<Q, R> mw) {
        DefaultContext<Q, R> prev = base.getPrev();
        prev.setNext(mw);
        mw.setPrev(prev);

        mw.setNext(base);
        base.setPrev(mw);
        return this;
    }

    private Container<Q, R> addAfter(DefaultContext<Q, R> base, DefaultContext<Q, R> mw) {
        DefaultContext<Q, R> next = base.getNext();
        base.setNext(mw);
        mw.setPrev(base);
        mw.setNext(next);
        next.setPrev(mw);
        return this;
    }

    @Override
    public Container<Q, R> addAfter(@Nonnull String afterName,
                                    String name, @Nonnull Handler<Q, R> handler) {
        DefaultContext<Q, R> base = (DefaultContext<Q, R>) get(afterName);
        if (base == null)
            throw new IllegalArgumentException(afterName + " not found");

        DefaultContext<Q, R> ctx = newMiddleware(name, handler);
        addAfter(base, ctx);
        return this;
    }

    @Override
    public Container<Q, R> addBefore(@Nonnull String beforeName,
                                     String name, @Nonnull Handler<Q, R> handler) {
        DefaultContext<Q, R> base = (DefaultContext<Q, R>) get(beforeName);
        if (base == null)
            throw new IllegalArgumentException(beforeName + " not found");

        DefaultContext<Q, R> ctx = newMiddleware(name, handler);
        addBefore(base, ctx);
        return this;
    }

    @Override
    public Context<Q, R> getFirst() {
        return head.getNext() == tail ? null : head.getNext();
    }

    @Override
    public Context<Q, R> getLast() {
        return tail.getPrev() == head ? null : tail.getPrev();
    }

    private Context<Q, R> findMatch(Predicate<Context<Q, R>> pred) {
        DefaultContext<Q, R> curr = head.getNext();
        while (curr != null && curr != tail) {
            if (pred.test(curr))
                return curr;
            curr = curr.getNext();
        }
        return null;
    }

    @Override
    public Context<Q, R> get(@Nonnull String name) {
        return findMatch(c -> name.equals(c.name()));
    }

    @Override
    public Context<Q, R> get(@Nonnull Handler<Q, R> handler) {
        return findMatch(c -> handler == c.handler());
    }

    private void remove(@Nonnull DefaultContext<Q, R> ctx) {
        DefaultContext<Q, R> prev = ctx.getPrev();
        DefaultContext<Q, R> next = ctx.getNext();
        prev.setNext(next);
        next.setPrev(prev);
        ctx.setPrev(null);
        ctx.setNext(null);
    }

    @Override
    public Context<Q, R> remove(@Nonnull String name) {
        DefaultContext<Q, R> base = (DefaultContext<Q, R>) get(name);
        if (base != null)
            remove(base);
        return base;
    }

    @Override
    public Context<Q, R> remove(@Nonnull Handler<Q, R> handler) {
        DefaultContext<Q, R> base = (DefaultContext<Q, R>) get(handler);
        if (base != null)
            remove(base);
        return base;
    }

    private static class HeadContext<Q, R> extends DefaultContext<Q, R> {
        public HeadContext() {
            super(null, null, null);
        }

        @Override
        DefaultContext<Q, R> getPrev() {
            throw new RuntimeException("should never reach here");
        }

        @Override
        void setPrev(DefaultContext<Q, R> prev) {
            throw new RuntimeException("should never reach here");
        }
    }

    private static class TailContext<Q, R> extends DefaultContext<Q, R> {
        public TailContext() {
            super(null, null, null);
        }

        @Override
        void setNext(DefaultContext<Q, R> next) {
            throw new RuntimeException("should never reach here");
        }
    }
}
