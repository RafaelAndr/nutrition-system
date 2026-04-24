package com.personal_finance.service;

import com.personal_finance.dto.account.*;
import com.personal_finance.entity.Account;
import com.personal_finance.entity.Users;
import com.personal_finance.exception.EntityAlreadyExistsException;
import com.personal_finance.mapper.AccountMapper;
import com.personal_finance.repository.AccountRepository;
import com.personal_finance.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final SecurityService securityService;
    private final AccountMapper accountMapper;

    private Users getLoggedUser(){
        return securityService.getUserLoggedIn();
    }

    public AccountResponseDto save(AccountRequestDto accountRequestDto){
        Users userLogged = getLoggedUser();

        log.info("User {} is creating an account with bank name '{}'",
                userLogged.getId(), accountRequestDto.bankName());

        Account account = accountMapper.toEntity(accountRequestDto);
        account.setUser(userLogged);

        if (existBankNameWithUser(account.getBankName())){
            log.warn("User {} tried to create duplicate bank name '{}'",
                    userLogged.getId(), account.getBankName());
            throw new EntityAlreadyExistsException("Bank name already exists");
        }

        account.setBalance(BigDecimal.ZERO);

        Account saved = accountRepository.save(account);

        log.info("Account {} created successfully for user {}",
                saved.getId(), userLogged.getId());

        return accountMapper.toDto(saved);
    }

    public Account searchById(UUID id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Account not found")
        );
    }

    private boolean existBankNameWithUser(String bankName){
        Users userLogged = getLoggedUser();
        return accountRepository.existsByBankNameAndUser(bankName, userLogged);
    }

    public void editBankName(UUID accountId, AccountRequestDto accountRequestDto){

        Users userLogged = getLoggedUser();
        Account account = searchById(accountId);

        account.validateOwnership(userLogged.getId());

        String oldName = account.getBankName();
        String newName = accountRequestDto.bankName();

        log.info("User {} is updating bank name from '{}' to '{}' for account {}",
                userLogged.getId(), oldName, newName, accountId);

        if (accountRepository.existsByBankNameAndUser(newName, userLogged)
                && !oldName.equalsIgnoreCase(newName)) {

            log.warn("User {} tried to update to an existing bank name '{}'",
                    userLogged.getId(), newName);

            throw new EntityAlreadyExistsException("Bank name already exists");
        }

        account.changeBankName(newName);
        accountRepository.save(account);

        log.info("Account {} bank name updated successfully", accountId);
    }

    public void delete(UUID id){
        Users userLogged = getLoggedUser();

        Account account = searchById(id);
        account.validateOwnership(userLogged.getId());

        log.info("User {} is deleting account {}",
                userLogged.getId(), id);

        accountRepository.deleteById(id);

        log.info("Account {} deleted successfully", id);
    }

    public List<AccountBalanceDto> getUserAccounts(){
        Users userLogged = getLoggedUser();

        List<Account> accounts = accountRepository.findByUser(userLogged);

        return accounts.stream().map(
                account -> new AccountBalanceDto(account.getId(), userLogged.getName(), account.getBalance(), account.getBankName()))
                .toList();
    }

    public AccountTotalBalanceDto getTotalUserBalance(){
        Users userLogged = getLoggedUser();

        List<Account> accounts = accountRepository.findByUser(userLogged);

        BigDecimal total = accounts.stream()
                .map(account -> account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AccountTotalBalanceDto(userLogged.getName(), total);
    }

    public void addAmount(UUID id, UpdateBalanceDto updateBalanceDto){
        Users userLogged = getLoggedUser();

        Account account = searchById(id);
        account.validateOwnership(userLogged.getId());

        log.info("User {} is adding {} to account {}",
                userLogged.getId(), updateBalanceDto.amount(), id);

        account.credit(updateBalanceDto.amount());

        accountRepository.save(account);

        log.info("New balance for account {} is {}",
                id, account.getBalance());
    }

    public void removeAmount(UUID id, UpdateBalanceDto updateBalanceDto){
        Users userLogged = getLoggedUser();

        Account account = searchById(id);
        account.validateOwnership(userLogged.getId());

        log.info("User {} is removing {} from account {}",
                userLogged.getId(), updateBalanceDto.amount(), id);

        account.debit(updateBalanceDto.amount());

        accountRepository.save(account);

        log.info("New balance for account {} is {}",
                id, account.getBalance());
    }
}