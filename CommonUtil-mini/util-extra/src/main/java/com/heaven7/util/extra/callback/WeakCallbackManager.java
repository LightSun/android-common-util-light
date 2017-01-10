package com.heaven7.util.extra.callback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * the callback manager. note the {@link Callback} must be strong reference.or else may be recycled.
 */
public abstract class WeakCallbackManager<T> extends CallbackManager<WeakReference<T>> {
    @Override
    protected void onTrim(ArrayList<WeakReference<T>> list, int maxCapacity) {
        ListIterator<WeakReference<T>> it = list.listIterator();
        for (; it.hasNext(); ) {
            final WeakReference<T> next = it.next();
            if (next.get() == null) {
                it.remove();
            }
        }
    }

    /**
     * a simple implements of WeakCallbackManager that use {@link Callback}.
     */
    public static class SimpleWeakCallbackManager extends WeakCallbackManager<Callback> {
        @Override
        protected void dispatchCallbackImpl(WeakReference<Callback> ref, Object param) {
            if (ref.get() != null) {
                ref.get().handleCallback(param);
            }
        }
    }


}
