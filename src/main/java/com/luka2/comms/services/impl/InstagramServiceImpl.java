package com.luka2.comms.services.impl;

import com.luka2.comms.http.SimpleClient;
import com.luka2.comms.models.Account;
import com.luka2.comms.models.SingleImagePost;
import com.luka2.comms.models.SimpleResponse;
import com.luka2.comms.services.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Service
public class InstagramServiceImpl implements InstagramService {

    static final String BASE = "https://graph.facebook.com";
    static final String VERSION = "v18.0";

    @Autowired
    SimpleClient client;

    @Value("staticDirectory")
    private String staticDirectory;

    @Value("staticUrl")
    private String staticUrl;

    public void createSingleImagePost(String image, String caption, Account account){
        SingleImagePost singleImagePost = new SingleImagePost();
        singleImagePost.setImage(image);
        singleImagePost.setCaption(caption);
        singleImagePost.setIgUserId( account.getIgUserId() );
        singleImagePost.setImageName( saveImage(image) );

        int containerId = createSingleImagePostContainer(singleImagePost);
        singleImagePost.setIgContainerId(containerId);

        int mediaId = createSingleImagePostMedia(singleImagePost);
        singleImagePost.setIgMediaId(mediaId);
    }

    private int createSingleImagePostContainer(SingleImagePost singleImagePost){
        Map<String, String> queryString = new HashMap<>();
        queryString.put("image_url", singleImagePost.getImageName());
        queryString.put("caption", singleImagePost.getCaption());

        String url = String.format("%s/%s/%s/%s?%s", BASE, VERSION, singleImagePost.getIgUserId(), "media", getQueryString(queryString));
        return client.post(url, "", SimpleResponse.class).body().getId();
    }

    private int createSingleImagePostMedia(SingleImagePost singleImagePost){
        Map<String, String> queryString = new HashMap<>();
        queryString.put("creation_id", String.valueOf(singleImagePost.getIgContainerId()));
        String url = String.format("%s/%s/%s/%s?%s", BASE, VERSION, singleImagePost.getIgUserId(), "media_publish", getQueryString(queryString));
        return client.post(url, "", SimpleResponse.class).body().getId();
    }

    private static String getQueryString(Map<String, String> queryStringMap) {
        if (queryStringMap == null || queryStringMap.isEmpty()) {
            return "";
        }

        try {
            StringJoiner joiner = new StringJoiner("&");

            for (Map.Entry<String, String> entry : queryStringMap.entrySet()) {
                String key = URLEncoder.encode(entry.getKey(), "UTF-8");
                String value = URLEncoder.encode(entry.getValue(), "UTF-8");

                joiner.add(key + "=" + value);
            }

            return joiner.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String saveImage(String imageContent){
        byte[] imageBytes = Base64.getDecoder().decode(imageContent);

        String fileName = imageContent.hashCode() + ".jpg";

        try (FileOutputStream fos = new FileOutputStream(staticDirectory + fileName)) {
            fos.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

}
