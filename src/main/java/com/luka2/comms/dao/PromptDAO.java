package com.luka2.comms.dao;

import com.luka2.comms.models.Prompt;
import com.luka2.comms.repositories.PromptRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@Transactional
public class PromptDAO {

    private final PromptRepository promptRepository;

    public PromptDAO(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    public Optional<Prompt> getById(Long id) {
        return promptRepository.findById(id);
    }

    public Optional<Set<Prompt>> getByAccountId(Long accountId){
        return promptRepository.findByAccountId(accountId);
    }

    public Optional<Set<Prompt>> getByAccountIdAndParentIdIsNull(Long accountId){
        return promptRepository.findByAccountIdAndParentIdIsNull(accountId);
    }

    public Optional<Prompt> getByPostId(Long postId){
        return promptRepository.findByPostId(postId);
    }

    public Prompt save(Prompt prompt){
        return promptRepository.save(prompt);
    }
}
