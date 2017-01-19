package com.heaven7.util.extra.collection;

/**
 * the result visitor
 * Created by heaven7 on 2017/1/16.
 */
public interface ResultVisitor<T, Result> {

    /**
     * called when visit the target element.
     *
     * @param t     the element
     * @param param the param data to carry
     * @return the result.
     */
    Result visit(T t, Object param);
}
