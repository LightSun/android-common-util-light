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
public class SynchronousVisitableList<T> implements CollectionVisitable<T> {

    private final CollectionVisitable<T> mImpl;
    private final Object mLock;

    public SynchronousVisitableList(CollectionVisitable<T> mImpl, Object mLock) {
        this.mImpl = mImpl;
        this.mLock = mLock;
    }

    @Override
    public <R> Collection<R> acceptVisit(@VisitResultRuleType int rule, Object param,
                                         ResultVisitor<T, R> visitor, @Nullable Collection<R> out) {
        synchronized (mLock) {
            return mImpl.acceptVisit(rule, param, visitor, out);
        }
    }

    @Override
    public boolean acceptVisit(@VisitRuleType int rule,
                               Object param, ElementVisitor<T> visitor) {
        synchronized (mLock) {
            return mImpl.acceptVisit(rule, param, visitor);
        }
    }

    @Override
    public T find(Object param, ElementPredicate<T> predicate) {
        synchronized (mLock) {
            return mImpl.find(param, predicate);
        }
    }

    @Override
    public <R> List<R> acceptVisitList(@VisitResultRuleType int rule, Object param,
                                       ResultVisitor<T, R> visitor, @Nullable List<R> out) {
        synchronized (mLock) {
            return mImpl.acceptVisitList(rule, param, visitor, out);
        }
    }
}
