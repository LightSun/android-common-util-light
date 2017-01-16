package com.heaven7.util.extra.collection;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.heaven7.util.extra.collection.CollectionConstant.*;

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
 *
 * @since 1.0.9
 */
//comes from ViewTreeObserver.CopyOnWriteArray
public class CopyOnWriteArray<T> {

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
        onTrim(mData);
    }

    /**
     * onTrim the list. this is called in {@link #end()}. you should care about the list.
     *
     * @param list the real list to onTrim.
     */
    protected void onTrim(ArrayList<T> list) {

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

    /**
     * get a visitable List.but it's not thread safe.
     * @return   a visitable List
     */
    public VisitableList<T> visitableList() {
        return new VisitableListImpl<>(this);
    }
    public VisitableList<T> safeVisitableList() {
        return new SynchronousVisitableList<T>(new VisitableListImpl<T>(this));
    }

    //==============================================


    private static class VisitableListImpl<T> extends AbstractVisitableList<T> {
        private final CopyOnWriteArray<T> mArray;

        public VisitableListImpl(CopyOnWriteArray<T> mArray) {
            this.mArray = mArray;
        }

        @Override
        public <R> List<R> acceptVisitList(@VisitResultRuleType int rule, Object param,
                                           ResultVisitor<T, R> visitor, @Nullable List<R> out) {
            if (out == null) {
                out = new ArrayList<>();
            }
            final CopyOnWriteArray.Access<T> access = mArray.start();
            try {
                final int size = access.size();
                R result;
                switch (rule) {
                    case VISIT_RESULT_RULE_UNTIL_NOT_NULL:
                        for (int i = 0; i < size; i++) {
                            result = visitor.visit(access.get(i), param);
                            if (result != null) {
                                out.add(result);
                                return out;
                            }
                        }
                        break;

                    case VISIT_RESULT_RULE_UNTIL_NULL:
                        for (int i = 0; i < size; i++) {
                            result = visitor.visit(access.get(i), param);
                            if (result != null) {
                                out.add(result);
                            } else {
                                return out;
                            }
                        }
                        break;

                    case VISIT_RULE_ALL:
                        for (int i = 0; i < size; i++) {
                            out.add(visitor.visit(access.get(i), param));
                        }
                        return out;

                    default:
                        throw new IllegalArgumentException("unsupport rule( " + rule + ")");
                }
            } finally {
                mArray.end();
            }
            return out;
        }

        @Override
        public boolean acceptVisit(@VisitRuleType int rule, Object param, ElementVisitor<T> visitor) {
            final CopyOnWriteArray.Access<T> access = mArray.start();
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

                    default:
                        throw new IllegalArgumentException("unsupport rule( " + rule + ")");
                }
            } finally {
                mArray.end();
            }
            return false;
        }

        @Override
        public T find(Object param, ElementPredicate<T> predicate) {
            final CopyOnWriteArray.Access<T> access = mArray.start();
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
                mArray.end();
            }
            return null;
        }
    }
}
