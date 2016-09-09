package com.heaven7.util.extra;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCallbackManager<T> {

    private List<T> mExecutors = new ArrayList<>(8);

    public void clear(){
        mExecutors.clear();
    }

    public void register(T t){
        if(!mExecutors.contains(t)) {
            mExecutors.add(t);
        }
    }

    public void unregister(T t){
         mExecutors.remove(t);
    }

    /**
     * dispatch callback
     * @param args the extra data
     */
    public abstract void dispatchCallback(Object...args);
}
