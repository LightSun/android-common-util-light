package com.heaven7.util.extra.collection;

/**
 * the element visitor of array or list.
 *
 * @param <T> the element type
 * @since 1.1.0
 */
public interface ElementVisitor<T> {
    /**
     * called when visit the target element.
     *
     * @param t     the element
     * @param param the param data to carry
     * @return true if visit success.
     */
    boolean visit(T t, Object param);
}
