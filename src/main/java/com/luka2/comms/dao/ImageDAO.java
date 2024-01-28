package com.luka2.comms.dao;

import com.luka2.comms.models.Image;
import com.luka2.comms.models.Post;
import com.luka2.comms.repositories.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class ImageDAO {

    private final ImageRepository imageRepository;

    public ImageDAO(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Optional<Image> getById(Long id) {
        return imageRepository.findById(id);
    }

    public Optional<Image> getByIgContainerId(Long igContainerId){
        return imageRepository.findByIgContainerId(igContainerId);
    }

    public List<Image> getByPostId(Long postId){
        return imageRepository.findByPostId(postId);
    }

    public Image save(Image image){
        return imageRepository.save(image);
    }
}
