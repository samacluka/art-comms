package com.luka2.comms.dao;

import com.luka2.comms.models.Account;
import com.luka2.comms.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Transactional
public class AccountDAO {

    private final AccountRepository accountRepository;

    public AccountDAO(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> getById(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> getByIgContainerId(Long igUserId){
        return accountRepository.findByIgUserId(igUserId);
    }

    public Account save(Account account){
        return accountRepository.save(account);
    }
}
