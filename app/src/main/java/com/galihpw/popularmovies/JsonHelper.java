package com.galihpw.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GalihPW on 10/07/2017.
 */

public class JsonHelper {
    public static String getStringJson(JSONObject json, String key){
        try {
            if(!json.isNull(key))
                return json.getString(key);
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static float getFloatJson(JSONObject json, String key){
        try {
            if(!json.isNull(key))
                return Float.parseFloat(json.getString(key));
            else
                return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getLongJson(JSONObject json, String key) throws NumberFormatException, JSONException {
        try {
            if(!json.isNull(key))
                return json.getLong(key);
            else
                return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getIntJson(JSONObject json, String key) throws NumberFormatException, JSONException{
        try {
            if(!json.isNull(key))
                return json.getInt(key);
            else
                return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean getBoolJson(JSONObject json, String key) throws NumberFormatException, JSONException{
        try {
            if(!json.isNull(key))
                return json.getBoolean(key);
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
