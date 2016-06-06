package com.heaven7.util.extra;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

/**
 * Created by heaven7 on 2016/5/10.
 */
public abstract class AbsSearchEditorActionImpl implements TextView.OnEditorActionListener {

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String keywords = v.getText().toString().trim();
        boolean isActionSearch =
                actionId != 0 ? (actionId == EditorInfo.IME_ACTION_SEARCH) :
                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
        if (!TextUtils.isEmpty(keywords) && isActionSearch) {
            onSearch(keywords);
            return true;
        }
        return onOtherEditorAction(keywords,actionId,event);
    }
//copy from OnEditorActionListener
    /**
     * if search action not occoured , this will be called.
     * @param keywords the keyword from the text view.
     * @param actionId Identifier of the action.  This will be either the
     * identifier you supplied, or {@link EditorInfo#IME_NULL
     * EditorInfo.IME_NULL} if being called due to the enter key
     * being pressed.
     * @param event If triggered by an enter key, this is the event;
     * otherwise, this is null.
     * @return Return true if you have consumed the action, else false.
     */
    protected boolean onOtherEditorAction(String keywords, int actionId, KeyEvent event) {
        return false;
    }

    /**
     * called when the seatch action occoured.
     * @param keywords the keywords
     */
    protected abstract void onSearch(String keywords);
}
