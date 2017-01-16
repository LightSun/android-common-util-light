package com.heaven7.util.extra.collection;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * the abstract implements of VisibleList.
 * Created by heaven7 on 2017/1/17.
 */
public abstract class AbstractVisitableList<T> implements VisitableList<T> {

    @Override
    public <R> Collection<R> acceptVisit(@CollectionConstant.VisitResultRuleType int rule,
                                         Object param, ResultVisitor<T, R> visitor,
                                         @Nullable Collection<R> out) {
        if (out == null) {
            return acceptVisitList(rule, param, visitor, null);
        } else {
            ArrayList<R> temp = new ArrayList<>();
            acceptVisitList(rule, param, visitor, temp);
            out.addAll(temp);
            return out;
        }
    }
}
