package com.luka2.comms.dao;

import com.luka2.comms.models.Post;
import com.luka2.comms.repositories.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Transactional
public class PostDAO {

    private final PostRepository postRepository;

    public PostDAO(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    public Optional<Post> getByPostId(Long accountId){
        return postRepository.findByAccountId(accountId);
    }

    public Optional<Post> getByIgMediaId(Long igMediaId){
        return postRepository.findByIgMediaId(igMediaId);
    }

    public Optional<Post> getByIgContainerId(Long igContainerId){
        return postRepository.findByIgContainerId(igContainerId);
    }

    public Post save(Post post){
        return postRepository.save(post);
    }
}
