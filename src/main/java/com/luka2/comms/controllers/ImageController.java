package com.luka2.comms.controllers;

import com.luka2.comms.dao.ImageDAO;
import com.luka2.comms.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping(value = "/images")
public class ImageController {

    @Autowired
    ImageDAO imageDAO;

    @GetMapping
    public byte[] getImage(
        @RequestParam Optional<Long> id,
        @RequestParam Optional<Long> igContainerId
    ) {
        Optional<Image> image;

        if(id.isPresent()) {
            image = imageDAO.getById(id.get());
        } else if(igContainerId.isPresent()) {
            image = imageDAO.getByIgContainerId(igContainerId.get());
        } else {
            return new byte[0];
        }

        return image.map(Image::getImageData).orElse(null);
    }

}
