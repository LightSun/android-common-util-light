package com.heaven7.util.extra.collection;

/**
 * the predicate of element
 *
 * @param <T> the  item type
 * @since 1.1.0
 */
public interface ElementPredicate<T> {

    /**
     * called when we want to predicate the item with the param.
     *
     * @param t     the item
     * @param param the other param
     * @return true if predicate ok.
     */
    boolean test(T t, Object param);
}
