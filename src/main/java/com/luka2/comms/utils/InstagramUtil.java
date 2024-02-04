package com.luka2.comms.utils;

import com.luka2.comms.models.Account;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public class InstagramUtil {

    private InstagramUtil() { throw new IllegalStateException("Utility class"); }

    public enum IG_ROUTES {
        MEDIA("media"),
        MEDIA_PUBLISH("media_publish");
        final String value;
        IG_ROUTES(String value) { this.value = value; }
    }

    @Value("instagram.protocol")
    private static String igProtocol;

    @Value("instagram.host")
    private static String igHost;

    @Value("instagram.version")
    private static String igVersion;

    @Value("instagram.client.id")
    private static String igClientId;

    @Value("instagram.client.secret")
    private static String igClientSecret;


    public static String getUrl(Account account, IG_ROUTES route, Map<String, String> queryString){
        queryString.put("access_token", String.format("%s|%s", igClientId, igClientSecret));
        return String.format("%s://%s/%s/%s/%s?%s", igProtocol, igHost, igVersion, account.getIgUserId(), route.value, HttpUtil.getQueryString(queryString));
    }

}
