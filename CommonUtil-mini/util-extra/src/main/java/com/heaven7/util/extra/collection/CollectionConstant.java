package com.heaven7.util.extra.collection;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * the constant of collections.
 * Created by heaven7 on 2017/1/15.
 * @since 1.1.0
 */
public class CollectionConstant {
    /**
     * the visit rule: until success.
     *
     * @since 1.1.0
     */
    public static final int VISIT_RULE_UNTIL_SUCCESS = 1;
    /**
     * the visit rule: until failed.
     *
     * @since 1.1.0
     */
    public static final int VISIT_RULE_UNTIL_FAILED = 2;
    /**
     * the visit rule: visit all.
     *
     * @since 1.1.0
     */
    public static final int VISIT_RULE_ALL = 3;

    @Retention(RetentionPolicy.CLASS)
    @IntDef({
            VISIT_RULE_UNTIL_SUCCESS,
            VISIT_RULE_UNTIL_FAILED,
            VISIT_RULE_ALL,
    })
    public @interface VisitRuleType {
    }

}
