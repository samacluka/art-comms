package com.luka2.comms.services.impl;

import com.luka2.comms.http.SimpleClient;
import com.luka2.comms.models.Account;
import com.luka2.comms.models.Image;
import com.luka2.comms.models.Post;
import com.luka2.comms.models.SimpleResponse;
import com.luka2.comms.services.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InstagramServiceImpl implements InstagramService {

    private static final String IG_BASE = "https://graph.facebook.com";
    private static final String IG_VERSION = "v18.0";

    private enum IG_ROUTES {
        MEDIA("media"),
        MEDIA_PUBLISH("media_publish");
        final String value;
        IG_ROUTES(String value) { this.value = value; }
    }

    @Autowired
    SimpleClient client;

    @Value("baseUrl")
    private String baseUrl;

    public void createPost(byte[] imageData, String caption, Account account) throws Exception {
        createPost(Collections.singletonList(imageData), caption, account);
    }

    public void createPost(Iterable<byte[]> imagesData, String caption, Account account) throws Exception {
        if(account == null || account.getIgUserId() == null) return;

        Set<Image> images = new HashSet<>();
        for(byte[] imageData : imagesData) {
            Image image = new Image();
            image.setImageData( imageData );
            images.add( image );
        }

        Post post = new Post();
        post.setImages( images );
        post.setCaption( caption );
        post.setAccount( account );

        account.getPosts().add( post );

        if(images.size() == 1) {
            Long containerId = createSingleImagePostContainer(post);
            post.setIgContainerId(containerId);

            Long mediaId = publishSingleImagePostMedia(post);
            post.setIgMediaId(mediaId);
        }
        else if(images.size() > 1 && images.size() <= 10){
            createCarouselItemContainers(post);

            Long containerId = createCarouselContainer(post);
            post.setIgContainerId(containerId);

            Long mediaId = publishCarouselContainer(post);
            post.setIgMediaId(mediaId);
        }
        else {
            throw new Exception("incorrect number of images");
        }
    }

    private void createCarouselItemContainers(Post post){
        for(Image image : post.getImages()){
            String retrievalUrl = baseUrl + String.format("/images?id=%s", image);

            Map<String, String> queryString = new HashMap<>();
            queryString.put("image_url", retrievalUrl);
            queryString.put("is_carousel_item ", "true");

            String url = getIgUrl(post, IG_ROUTES.MEDIA, queryString);
            Long containerId = client.post(url, null, SimpleResponse.class).body().getId();

            image.setIgContainerId(containerId);
        }
    }

    private Long createCarouselContainer(Post post){
        String imageContainerIds = post.getImageContainerIds().stream().map(String::valueOf).collect(Collectors.joining(","));

        Map<String, String> queryString = new HashMap<>();
        queryString.put("media_type", "CAROUSEL");
        queryString.put("children ", imageContainerIds);
        String url = getIgUrl(post, IG_ROUTES.MEDIA, queryString);
        return client.post(url, null, SimpleResponse.class).body().getId();
    }

    private Long publishCarouselContainer(Post post){
        Map<String, String> queryString = new HashMap<>();
        queryString.put("creation_id ", String.valueOf(post.getIgContainerId()));
        String url = getIgUrl(post, IG_ROUTES.MEDIA_PUBLISH, queryString);
        return client.post(url, null, SimpleResponse.class).body().getId();
    }

    private Long createSingleImagePostContainer(Post post){
        String retrievalUrl = baseUrl + String.format("/images?id=%s", post.getImages().stream().toList().get(0).getId());

        Map<String, String> queryString = new HashMap<>();
        queryString.put("image_url", retrievalUrl);
        queryString.put("caption", post.getCaption());

        String url = getIgUrl(post, IG_ROUTES.MEDIA, queryString);
        return client.post(url, null, SimpleResponse.class).body().getId();
    }

    private Long publishSingleImagePostMedia(Post post){
        Map<String, String> queryString = new HashMap<>();
        queryString.put("creation_id", String.valueOf(post.getIgContainerId()));
        String url = getIgUrl(post, IG_ROUTES.MEDIA_PUBLISH, queryString);
        return client.post(url, null, SimpleResponse.class).body().getId();
    }

    private static String getIgUrl(Post post, IG_ROUTES route, Map<String, String> queryString){
        return String.format("%s/%s/%s/%s?%s", IG_BASE, IG_VERSION, post.getAccount().getIgUserId(), route.value, getQueryString(queryString));
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

}
