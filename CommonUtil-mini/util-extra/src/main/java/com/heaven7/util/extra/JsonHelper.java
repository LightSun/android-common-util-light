package com.heaven7.util.extra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * json helper
 * @author heaven7
 */
public final class JsonHelper {

    /** the root JSONObjectHelper */
    private final JSONObjectHelper mHelper = new JSONObjectHelper();

    /**
     * create a JSONObjectHelper
     */
    public static JSONObjectHelper newJSONObjectHelper(){
        return new JSONObjectHelper();
    }

    /**
     * create a JSONArrayHelper
     */
    public static JSONArrayHelper newJSONArrayHelper(){
        return new JSONArrayHelper();
    }

    /**
     * @param key the string key
     * @param value a {@link JSONObject}, {@link JSONArray}, String, Boolean,
     *              Integer, Long, Double, JSONObject#NULL, or {@code null}. May not be
     *              {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
     *              infinities} or {@link JSONObjectHelper} or {@link JSONArrayHelper}.
     */
    public JsonHelper put(String key, Object value) {
        if (value instanceof JSONObjectHelper) {
            mHelper.put(key, ((JSONObjectHelper) value).toJSONObject());
        } else if (value instanceof JSONArrayHelper) {
            mHelper.put(key, ((JSONArrayHelper) value).toJSONArray());
        } else {
            mHelper.put(key, value);
        }
        return this;
    }

    public JSONObject toJSONObject() {
        return mHelper.toJSONObject();
    }
    public String toJson(){
        return mHelper.toString();
    }

    public static class JSONArrayHelper {
        private final JSONArray mJsonArray = new JSONArray();

        private JSONArrayHelper() {}

        /**
         * @param value a {@link JSONObject}, {@link JSONArray}, String, Boolean,
         *              Integer, Long, Double, JSONObject#NULL, or {@code null}. May not be
         *              {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
         *              infinities} or {@link JSONObjectHelper} or {@link JSONArrayHelper}.
         */
        public JSONArrayHelper put(Object value) {
            if(value instanceof JSONArrayHelper){
                mJsonArray.put(((JSONArrayHelper) value).toJSONArray());
            }else if(value instanceof JSONObjectHelper) {
                mJsonArray.put(((JSONObjectHelper) value).toJSONObject());
            }else{
                mJsonArray.put(value);
            }
            return this;
        }

        public JSONArrayHelper put(int index, Object value) {
            try {
                mJsonArray.put(index, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public JSONArray toJSONArray() {
            return mJsonArray;
        }

        @Override
        public String toString() {
            return mJsonArray.toString();
        }
    }

    public static class JSONObjectHelper {
        private final JSONObject jobj = new JSONObject();
        private JSONObjectHelper() {}

        /**
         * @param value a {@link JSONObject}, {@link JSONArray}, String, Boolean,
         *              Integer, Long, Double, JSONObject#NULL, or {@code null}. May not be
         *              {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
         *              infinities} or {@link JSONObjectHelper} or {@link JSONArrayHelper}.
         */
        public JSONObjectHelper put(String key, Object value) {
            try {
                if (value instanceof JSONObjectHelper) {
                    jobj.put(key, ((JSONObjectHelper) value).toJSONObject());
                } else if (value instanceof JSONArrayHelper) {
                    jobj.put(key, ((JSONArrayHelper) value).toJSONArray());
                } else {
                    jobj.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public JSONObject toJSONObject() {
            return jobj;
        }

        @Override
        public String toString() {
            return jobj.toString();
        }
    }


}
