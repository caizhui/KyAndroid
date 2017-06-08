package com.ky.kyandroid.util;

import com.solidfire.gson.Gson;

/**
 * Created by Administrator on 2017-3-2.
 */

public class JsonUtil {

    public static Gson mGson = new Gson();

    /**
     *
     * @param str
     * @param clz
     * @return 当str为null或空字符串时，返回null
     */
    public static Object stringToObject(String str,Class clz){
        return mGson.fromJson(str,clz);
    }


    public static String objectToString(Object obj){
        return mGson.toJson(obj);
    }


}
