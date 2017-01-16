package com.heaven7.util.extra.collection;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.List;

import static com.heaven7.util.extra.collection.CollectionConstant.VisitResultRuleType;
import static com.heaven7.util.extra.collection.CollectionConstant.VisitRuleType;

/**
 * a synchronous VisitableList.
 * Created by heaven7 on 2017/1/16.
 */
public class SynchronousVisitableList<T> implements VisitableList<T> {

    private final VisitableList<T> mImpl;

    public SynchronousVisitableList(VisitableList<T> mImpl) {
        this.mImpl = mImpl;
    }

    @Override
    public synchronized <R> Collection<R> acceptVisit(@VisitResultRuleType int rule,
                                                      Object param, ResultVisitor<T, R> visitor, @Nullable Collection<R> out) {
        return mImpl.acceptVisit(rule, param, visitor, out);
    }

    @Override
    public synchronized boolean acceptVisit(@VisitRuleType int rule,
                                            Object param, ElementVisitor<T> visitor) {
        return mImpl.acceptVisit(rule, param, visitor);
    }

    @Override
    public synchronized T find(Object param, ElementPredicate<T> predicate) {
        return mImpl.find(param, predicate);
    }

    @Override
    public synchronized <R> List<R> acceptVisitList(@VisitResultRuleType int rule, Object param,
                                                    ResultVisitor<T, R> visitor, @Nullable List<R> out) {
        return mImpl.acceptVisitList(rule, param, visitor, out);
    }
}
