package com.heaven7.util.extra.collection;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * visitable list
 * Created by heaven7 on 2017/1/16.
 *
 * @since 1.1.3
 */
public interface CollectionVisitable<T> {

    /**
     * accept the Result visitor visit.
     *
     * @param rule    the rule of result visitor. {@link CollectionConstant#VISIT_RESULT_RULE_UNTIL_NULL} and etc.
     * @param param   the extra param
     * @param visitor the result visitor
     * @param out     the out list. can be null.
     * @param <R>     the result type  of visit.
     * @return the collection of visit result
     */
    <R> Collection<R> acceptVisit(@CollectionConstant.VisitResultRuleType int rule,
                                  Object param, ResultVisitor<T, R> visitor, @Nullable Collection<R> out);

    /**
     * accept the visitor visit.
     *
     * @param rule    the visit rule, {@link CollectionConstant#VISIT_RULE_UNTIL_SUCCESS} and etc.
     * @param visitor the element visitor
     * @param param   the param data when visit.
     * @return true if visit success base on the rule.
     * @since 1.1.0
     */
    boolean acceptVisit(@CollectionConstant.VisitRuleType int rule, Object param, ElementVisitor<T> visitor);

    /**
     * find a element by the target param and predicate.
     *
     * @param param     the param.
     * @param predicate the element predicate
     * @return the target element by find in array.
     * @since 1.1.0
     */
    T find(Object param, ElementPredicate<T> predicate);


    <R> List<R> acceptVisitList(@CollectionConstant.VisitResultRuleType int rule,
                                Object param, ResultVisitor<T, R> visitor, @Nullable List<R> out);
}
