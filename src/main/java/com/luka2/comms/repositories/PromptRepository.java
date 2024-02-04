package com.luka2.comms.repositories;

import com.luka2.comms.models.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {

    Optional<Prompt> findById(Long id);
    Optional<Set<Prompt>> findByAccountId(Long accountId);
    Optional<Set<Prompt>> findByAccountIdAndParentIdIsNull(Long accountId);
    Optional<Prompt> findByPostId(Long postId);

}
