package com.heaven7.util.extra.collection;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Copy on write array. This array is not thread safe, and only one loop can
 * iterate over this array at any given time. This class avoids allocations
 * until a concurrent modification happens.
 * <p/>
 * Usage:
 * <p/> <pre>
 * CopyOnWriteArray.Access<MyData> access = array.start();
 * try {
 * for (int i = 0; i < access.size(); i++) {
 * MyData d = access.get(i);
 * }
 * } finally {
 * access.end();
 * }
 * </pre>
 * @since 1.0.9
 */
//comes from ViewTreeObserver.CopyOnWriteArray
public class CopyOnWriteArray<T> {

    /**
     * the visit rule: until success.
     * @since 1.1.0
     */
    public static final int VISIT_RULE_UNTIL_SUCCESS = 1;
    /**
     * the visit rule: until failed.
     * @since 1.1.0
     */
    public static final int VISIT_RULE_UNTIL_FAILED = 2;
    /**
     * the visit rule: visit all.
     * @since 1.1.0
     */
    public static final int VISIT_RULE_ALL = 3;

    @Retention(RetentionPolicy.CLASS)
    @IntDef({
            VISIT_RULE_UNTIL_SUCCESS,
            VISIT_RULE_UNTIL_FAILED,
            VISIT_RULE_ALL,
    })
    @interface VisitRuleType {
    }


    private ArrayList<T> mData = new ArrayList<T>();
    private ArrayList<T> mDataCopy;

    private final Access<T> mAccess = new Access<T>();

    private boolean mStart;

    public static class Access<T> {
        private ArrayList<T> mData;
        private int mSize;

        public T get(int index) {
            return mData.get(index);
        }

        public int size() {
            return mSize;
        }

        public boolean contains(T t) {
            return mData.contains(t);
        }

        public int indexOf(T t) {
            return mData.indexOf(t);
        }
    }

    public CopyOnWriteArray() {
    }

    public static <T> CopyOnWriteArray<T> create(List<T> list) {
        CopyOnWriteArray<T> array = new CopyOnWriteArray<>();
        array.addAll(list);
        return array;
    }

    protected ArrayList<T> getArray() {
        if (mStart) {
            if (mDataCopy == null) mDataCopy = new ArrayList<T>(mData);
            return mDataCopy;
        }
        return mData;
    }

    public Access<T> start() {
        if (mStart) throw new IllegalStateException("Iteration already started");
        mStart = true;
        mDataCopy = null;
        mAccess.mData = mData;
        mAccess.mSize = mData.size();
        return mAccess;
    }

    public void end() {
        if (!mStart) throw new IllegalStateException("Iteration not started");
        mStart = false;
        if (mDataCopy != null) {
            mData = mDataCopy;
            mAccess.mData.clear();
            mAccess.mSize = 0;
        }
        mDataCopy = null;
        trim(mData);
    }

    /**
     * trim the list. this is called in {@link #end()}. you should care about the list.
     *
     * @param list the real list to trim.
     */
    protected void trim(ArrayList<T> list) {

    }
    //===============================================================

    public boolean contains(T t) {
        return getArray().contains(t);
    }

    public int size() {
        return getArray().size();
    }

    public boolean add(T item) {
        return getArray().add(item);
    }

    public void addAll(CopyOnWriteArray<T> array) {
        getArray().addAll(array.mData);
    }

    public void addAll(List<T> list) {
        getArray().addAll(list);
    }

    public boolean remove(T item) {
        return getArray().remove(item);
    }

    public void clear() {
        getArray().clear();
    }
    //==============================================

    /**
     * accept the visitor visit.
     *
     * @param rule    the visit rule, {@link #VISIT_RULE_UNTIL_SUCCESS} and etc.
     * @param visitor the element visitor
     * @param param   the param data when visit.
     * @return true if visit success base on the rule.
     * @since 1.1.0
     */
    public boolean acceptVisit(@VisitRuleType int rule, Object param, ElementVisitor<T> visitor) {
        final CopyOnWriteArray.Access<T> access = start();
        try {
            final int size = access.size();
            switch (rule) {
                case VISIT_RULE_UNTIL_SUCCESS:
                    for (int i = 0; i < size; i++) {
                        if (visitor.visit(access.get(i), param)) {
                            return true;
                        }
                    }
                    break;

                case VISIT_RULE_UNTIL_FAILED:
                    for (int i = 0; i < size; i++) {
                        if (!visitor.visit(access.get(i), param)) {
                            return true;
                        }
                    }
                    break;

                case VISIT_RULE_ALL:
                    for (int i = 0; i < size; i++) {
                        visitor.visit(access.get(i), param);
                    }
                    return true;
            }
        } finally {
            end();
        }
        return false;
    }

    /**
     * find a element by the target param and predicate.
     *
     * @param param     the param.
     * @param predicate the element predicate
     * @return the target element by find in array.
     * @since 1.1.0
     */
    public T find(Object param, ElementPredicate<T> predicate) {
        final CopyOnWriteArray.Access<T> access = start();
        try {
            final int size = access.size();
            T t;
            for (int i = 0; i < size; i++) {
                t = access.get(i);
                if (predicate.test(t, param)) {
                    return t;
                }
            }
        } finally {
            end();
        }
        return null;
    }

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


}
