package io.github.sokrato.mw;

import java.util.ArrayList;
import java.util.List;

public class LoggingMW<Q, R> implements Handler<Q, R> {

    final List<Q> reqList = new ArrayList<>();
    final List<R> resList = new ArrayList<>();

    final List<String> names = new ArrayList<>();

    @Override
    public R handle(Q req, Context<? super Q, ? extends R> context) {
        reqList.add(req);
        names.add(context.name());
        R res = context.proceed(req);
        resList.add(res);
        return res;
    }
}
