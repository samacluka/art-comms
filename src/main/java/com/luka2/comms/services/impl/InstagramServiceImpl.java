package com.luka2.comms.services.impl;

import com.luka2.comms.dao.AccountDAO;
import com.luka2.comms.dao.ImageDAO;
import com.luka2.comms.dao.PostDAO;
import com.luka2.comms.models.Account;
import com.luka2.comms.models.Image;
import com.luka2.comms.models.Post;
import com.luka2.comms.models.IdResponse;
import com.luka2.comms.services.InstagramService;
import com.luka2.comms.utils.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.luka2.comms.utils.InstagramUtil.*;

@Service
public class InstagramServiceImpl implements InstagramService {

    @Value("server.address")
    private String address;

    @Value("server.port")
    private String port;

    @Autowired
    HttpClientServiceImpl client;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    ImageDAO imageDAO;

    @Autowired
    PostDAO postDAO;

    public Account createPost(Image imageData, String caption, Account account) {
        return createPost(Collections.singleton(imageData), caption, account);
    }

    public Account createPost(Set<Image> images, String caption, Account account) {
        if(account == null || account.getIgUserId() == null) return null;
        if(Post.validNumImages(GeneralUtil.getCount(images))) return null;

        Post post = new Post();
        post.setCaption( caption );
        post.setAccount( account );
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

            String url = getUrl(post.getAccount(), IG_ROUTES.MEDIA, queryString);
            Long containerId = client.post(url, null, IdResponse.class).body().getId();

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

        String url = getUrl(post.getAccount(), IG_ROUTES.MEDIA, queryString);
        Long containerId = client.post(url, null, IdResponse.class).body().getId();
        post.setIgContainerId(containerId);
    }

    private void publishPost(Post post){
        Map<String, String> queryString = new HashMap<>();
        queryString.put("creation_id", String.valueOf(post.getIgContainerId()));
        String url = getUrl(post.getAccount(), IG_ROUTES.MEDIA_PUBLISH, queryString);
        Long mediaId = client.post(url, null, IdResponse.class).body().getId();
        post.setIgMediaId(mediaId);
    }
}
