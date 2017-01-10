package com.heaven7.util.extra.collection;

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
        public int indexOf(T t){
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
     * @param list the real list to trim.
     */
    protected void trim(ArrayList<T> list){

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

}
