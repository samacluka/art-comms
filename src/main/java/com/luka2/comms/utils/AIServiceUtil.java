package com.luka2.comms.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public class AIServiceUtil {

    private AIServiceUtil() { throw new IllegalStateException("Utility class"); }

    public enum AI_SERVICE_ROUTES {
        IMAGES("image"),
        PROMPTS("prompts");
        final String value;
        AI_SERVICE_ROUTES(String value) { this.value = value; }
    }

    @Value("ai.service.protocol")
    private static String aiProtocol;

    @Value("ai.service.host")
    private static String aiHost;

    @Value("ai.service.port")
    private static String aiPort;


    public static String getUrl(AI_SERVICE_ROUTES route, Map<String, String> queryStringMap){
        String protocol = aiProtocol == null ? "" : aiProtocol+"://";

        String port = aiPort == null ? "" : ":"+aiPort;

        String queryString = HttpUtil.getQueryString(queryStringMap);
        queryString = queryString.isEmpty() ? "" : "?"+queryString;

        return String.format("%s%s%s/%s%s", protocol, aiHost, port, route.value, queryString);
    }

}
