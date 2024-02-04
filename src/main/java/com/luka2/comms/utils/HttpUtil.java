package com.luka2.comms.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public class HttpUtil {

    private HttpUtil() { throw new IllegalStateException("Utility class"); }

    public static String getQueryString(Map<String, String> queryStringMap) {
        if (queryStringMap == null || queryStringMap.isEmpty()) return "";

        StringJoiner joiner = new StringJoiner("&");

        for (Map.Entry<String, String> entry : queryStringMap.entrySet()) {
            String key = URLEncoder.encode(entry.getKey().trim(), StandardCharsets.UTF_8);
            String value = URLEncoder.encode(entry.getValue().trim(), StandardCharsets.UTF_8);

            joiner.add(key + "=" + value);
        }

        return joiner.toString();
    }

}
