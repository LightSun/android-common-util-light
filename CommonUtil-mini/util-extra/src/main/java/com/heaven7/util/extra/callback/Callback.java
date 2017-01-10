package com.heaven7.util.extra.callback;

/**
 * the callback interface
 */
public interface Callback {

    /**
     * called in {@link WeakCallbackManager#dispatchCallback(Object)} .
     *
     * @param param the extra param
     */
    void handleCallback(Object param);
}
