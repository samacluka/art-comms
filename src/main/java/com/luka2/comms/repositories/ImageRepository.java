package com.luka2.comms.repositories;

import com.luka2.comms.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findById(Long id);
    Optional<Image> findByIgContainerId(Long igContainerId);
    List<Image> findByPostId(Long postId);

}
