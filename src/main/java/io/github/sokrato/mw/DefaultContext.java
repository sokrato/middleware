package io.github.sokrato.mw;

public class DefaultContext<Q, R> implements Context<Q, R> {
    private final String name;
    private final Handler<Q, R> handler;
    private final Pipeline<Q, R> pipeline;
    private DefaultContext<Q, R> next = null;

    private DefaultContext<Q, R> prev = null;

    public DefaultContext(String name,
                          Handler<Q, R> handler,
                          Pipeline<Q, R> pipeline) {
        this.name = name;
        this.handler = handler;
        this.pipeline = pipeline;
    }

    public String name() {
        return name;
    }

    @Override
    public Handler<Q, R> handler() {
        return handler;
    }

    DefaultContext<Q, R> getNext() {
        return next;
    }

    void setNext(DefaultContext<Q, R> next) {
        this.next = next;
    }

    DefaultContext<Q, R> getPrev() {
        return prev;
    }

    void setPrev(DefaultContext<Q, R> prev) {
        this.prev = prev;
    }

    @Override
    public <T> T getAttr(String key) {
        return pipeline.getAttr(key);
    }

    @Override
    public <T> T getAttr(String key, T defaultVal) {
        return pipeline.getAttr(key, defaultVal);
    }

    @Override
    public <T> T setAttr(String key, Object obj) {
        return pipeline.setAttr(key, obj);
    }

    @Override
    public <T> T delAttr(String key) {
        return pipeline.delAttr(key);
    }

    @Override
    public R proceed(Q req) {
        Handler<Q, R> nh;
        if (next == null || (nh = next.handler()) == null) {
            throw new NoMoreHandlerException();
        }
        return nh.handle(req, next);
    }

    @Override
    public Pipeline<Q, R> pipeline() {
        return pipeline;
    }
}
