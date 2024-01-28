package com.luka2.comms.repositories;

import com.luka2.comms.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);
    Optional<Post> findByAccountId(Long accountId);
    Optional<Post> findByIgMediaId(Long igMediaId);
    Optional<Post> findByIgContainerId(Long igContainerId);

}
