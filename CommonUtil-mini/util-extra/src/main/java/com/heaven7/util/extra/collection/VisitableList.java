package com.heaven7.util.extra.collection;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * visitable list
 * Created by heaven7 on 2017/1/16.
 * @since 1.1.3
 */
public interface VisitableList<T> extends VisitableCollection<T> {

    <R> List<R> acceptVisitList(@CollectionConstant.VisitResultRuleType int rule,
                            Object param, ResultVisitor<T, R> visitor, @Nullable List<R> out);
}
