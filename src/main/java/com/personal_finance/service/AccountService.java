package com.personal_finance.service;
import com.personal_finance.entity.Account;
import com.personal_finance.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account save(Account account){
        return accountRepository.save(account);
    }

    public Account searchById(UUID id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Account not found")
        );
    }

    public void delete(UUID id){
        accountRepository.deleteById(id);
    }
}
