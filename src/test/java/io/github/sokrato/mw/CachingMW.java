package io.github.sokrato.mw;

import java.util.HashMap;
import java.util.Map;

public class CachingMW<Q, R> implements Handler<Q, R> {

    private final Map<Q, R> cache = new HashMap<>();

    @Override
    public R handle(Q req, Context<? super Q, ? extends R> context) {
        if (cache.containsKey(req))
            return cache.get(req);
        R res = context.proceed(req);
        cache.put(req, res);
        return res;
    }
}
