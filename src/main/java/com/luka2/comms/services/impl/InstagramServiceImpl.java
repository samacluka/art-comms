package com.luka2.comms.services.impl;

import com.luka2.comms.dao.AccountDAO;
import com.luka2.comms.dao.ImageDAO;
import com.luka2.comms.dao.PostDAO;
import com.luka2.comms.models.Account;
import com.luka2.comms.models.Image;
import com.luka2.comms.models.Post;
import com.luka2.comms.models.SimpleResponse;
import com.luka2.comms.services.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InstagramServiceImpl implements InstagramService {

    private static final String IG_PROTOCOL = "https";
    private static final String IG_HOST = "graph.facebook.com";
    private static final String IG_VERSION = "v18.0";

    private enum IG_ROUTES {
        MEDIA("media"),
        MEDIA_PUBLISH("media_publish");
        final String value;
        IG_ROUTES(String value) { this.value = value; }
    }

    @Value("server.address")
    private String address;

    @Value("server.port")
    private String port;

    @Value("instagram.client.id")
    private String igClientId;

    @Value("instagram.client.secret")
    private String igClientSecret;

    @Autowired
    HttpClientServiceImpl client;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    ImageDAO imageDAO;

    @Autowired
    PostDAO postDAO;

    public Account createPost(byte[] imageData, String caption, Account account) {
        return createPost(Collections.singletonList(imageData), caption, account);
    }

    public Account createPost(Iterable<byte[]> imagesData, String caption, Account account) {
        if(account == null || account.getIgUserId() == null) return null;
        if(Post.validNumImages(getCount(imagesData))) return null;

        Post post = new Post();
        post.setCaption( caption );
        post.setAccount( account );

        Set<Image> images = new HashSet<>();
        for(byte[] imageData : imagesData) {
            Image image = new Image();
            image.setImageData( imageData );
            images.add( image );
            image.setPost( post );
            imageDAO.save(image);
        }

        post.setImages( images );

        account.setPosts( Collections.singleton(post) );

        createItemContainers(post);
        if(post.hasMultipleImages()) createCarouselContainer(post);
        publishPost(post);

        return accountDAO.save(account);
    }

    private void createItemContainers(Post post){
        for(Image image : post.getImages()){
            String retrievalUrl = String.format("%s:%s/images?id=%s", address, port, image.getId());

            Map<String, String> queryString = new HashMap<>();
            queryString.put("image_url", retrievalUrl);

            if(post.hasMultipleImages())    queryString.put("is_carousel_item", "true");
            else                            queryString.put("caption", post.getCaption());

            String url = getIgUrl(post.getAccount(), IG_ROUTES.MEDIA, queryString);
            Long containerId = client.post(url, null, SimpleResponse.class).body().getId();

            if(post.hasMultipleImages())    image.setIgContainerId(containerId);
            else                            post.setIgContainerId(containerId);
        }
    }

    private void createCarouselContainer(Post post){
        String imageContainerIds = post.getImageContainerIds().stream().map(String::valueOf).collect(Collectors.joining(","));

        Map<String, String> queryString = new HashMap<>();
        queryString.put("media_type", "CAROUSEL");
        queryString.put("caption", post.getCaption());
        queryString.put("children", imageContainerIds);

        String url = getIgUrl(post.getAccount(), IG_ROUTES.MEDIA, queryString);
        Long containerId = client.post(url, null, SimpleResponse.class).body().getId();
        post.setIgContainerId(containerId);
    }

    private void publishPost(Post post){
        Map<String, String> queryString = new HashMap<>();
        queryString.put("creation_id", String.valueOf(post.getIgContainerId()));
        String url = getIgUrl(post.getAccount(), IG_ROUTES.MEDIA_PUBLISH, queryString);
        Long mediaId = client.post(url, null, SimpleResponse.class).body().getId();
        post.setIgMediaId(mediaId);
    }

    private String getIgUrl(Account account, IG_ROUTES route, Map<String, String> queryString){
        queryString.put("access_token", String.format("%s|%s", igClientId, igClientSecret));
        return String.format("%s://%s/%s/%s/%s?%s", IG_PROTOCOL, IG_HOST, IG_VERSION, account.getIgUserId(), route.value, getQueryString(queryString));
    }

    private String getQueryString(Map<String, String> queryStringMap) {
        if (queryStringMap == null || queryStringMap.isEmpty()) return "";

        StringJoiner joiner = new StringJoiner("&");

        for (Map.Entry<String, String> entry : queryStringMap.entrySet()) {
            String key = URLEncoder.encode(entry.getKey().trim(), StandardCharsets.UTF_8);
            String value = URLEncoder.encode(entry.getValue().trim(), StandardCharsets.UTF_8);

            joiner.add(key + "=" + value);
        }

        return joiner.toString();
    }

    private <T> int getCount(Iterable<T> collection){
        int counter = 0;
        for(T ignored : collection) counter++;
        return counter;
    }
}
