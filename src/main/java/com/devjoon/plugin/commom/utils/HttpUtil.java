package com.devjoon.plugin.commom.utils;

import org.elasticsearch.rest.RestRequest;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HttpUtil {

    public static JSONObject parseRequestBody(RestRequest request) {
        JSONObject ret = new JSONObject();
        try {
            ret = new JSONObject(new JSONTokener(request.content().utf8ToString()));
        } catch (Exception ignore) {
        }
        return ret;
    }


}
