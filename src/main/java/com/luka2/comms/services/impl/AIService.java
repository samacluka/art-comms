package com.luka2.comms.services.impl;

import com.luka2.comms.dao.ImageDAO;
import com.luka2.comms.dao.PromptDAO;
import com.luka2.comms.dao.SettingDAO;
import com.luka2.comms.models.*;
import com.luka2.comms.utils.AIServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AIService {

    @Value("server.address")
    private String address;

    @Value("server.port")
    private String port;

    @Autowired
    HttpClientServiceImpl client;

    @Autowired
    PromptDAO promptDAO;

    @Autowired
    SettingDAO settingDAO;

    @Autowired
    ImageDAO imageDAO;

    public Image image(Prompt prompt){
        Map<String, String> queryStringMap = new HashMap<>();
        queryStringMap.put("prompt", prompt.getText());
        String url = AIServiceUtil.getUrl(AIServiceUtil.AI_SERVICE_ROUTES.IMAGES, queryStringMap);

        String base64Image = client.get(url, ImageResponse.class).body().getB64image();

        byte[] imageData = Base64.getDecoder().decode(base64Image.getBytes(StandardCharsets.UTF_8));

        Image image = new Image();
        image.setImageData( imageData );
        return imageDAO.save(image);
    }

    public Set<Prompt> prompts(Account account){
        Set<Prompt> masters = promptDAO.getByAccountIdAndParentIdIsNull(account.getId()).orElse(new HashSet<>());
        int numChildren = Math.abs( settingDAO.get().getNumChildrenInPromptGeneration() );

        Map<String, String> queryStringMap = new HashMap<>();
        for(Prompt master : masters){
            queryStringMap.put("mother", master.getText());
            queryStringMap.put("num_children", String.valueOf(numChildren));
            String url = AIServiceUtil.getUrl(AIServiceUtil.AI_SERVICE_ROUTES.PROMPTS, queryStringMap);

            List<String> children = client.get(url, PromptResponse.class).body().getChildren();

            Set<Prompt> childrenToSave = new HashSet<>();
            for(String text : children){
                Prompt child = new Prompt();
                child.setText( text );
                child.setAccount( account );
                child.setParent( master );

                childrenToSave.add( child );
            }
            master.setChildren( childrenToSave );
            promptDAO.save( master );
        }

        return masters;
    }



}
