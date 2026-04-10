package com.personal_finance.service;

import com.personal_finance.dto.account.AccountBalanceDto;
import com.personal_finance.dto.account.AccountRequestDto;
import com.personal_finance.dto.account.AccountTotalBalanceDto;
import com.personal_finance.dto.account.UpdateBalanceDto;
import com.personal_finance.entity.Account;
import com.personal_finance.entity.Users;
import com.personal_finance.exception.AccessForbiddenException;
import com.personal_finance.exception.AccountHasNoUserException;
import com.personal_finance.exception.EntityAlreadyExistsException;
import com.personal_finance.repository.AccountRepository;
import com.personal_finance.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final SecurityService securityService;

    public Account save(Account account){
        Users userLogged = securityService.getUserLoggedIn();
        account.setUser(userLogged);

        if (existBankName(account.getBankName())){
            throw new EntityAlreadyExistsException("Bank name already exists");
        }

        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    public Account searchById(UUID id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Account not found")
        );
    }

    private boolean existBankName(String bankName){
        return accountRepository.existsByBankName(bankName);
    }

    public void editBankName(UUID accountId, AccountRequestDto accountRequestDto){
        Account account = searchById(accountId);

        account.setBankName(accountRequestDto.bankName());

        accountRepository.save(account);
    }

    public void delete(UUID id) throws AccessDeniedException{
        Users userLoggedIn = securityService.getUserLoggedIn();

        Account account = searchById(id);

        if (account.getUser() == null){
            throw new AccountHasNoUserException("Account has no user associated");
        }

        if (!account.getUser().getId().equals(userLoggedIn.getId())){
            throw new AccessDeniedException("You are not allowed to delete this account");
        }

        accountRepository.deleteById(id);
    }

    public List<AccountBalanceDto> getUserAccounts(){
        Users userLoggedIn = securityService.getUserLoggedIn();

        List<Account> accounts = accountRepository.findByUser(userLoggedIn);

        return accounts.stream().map(
                account -> new AccountBalanceDto(account.getId(), userLoggedIn.getName(), account.getBalance(), account.getBankName()))
                .toList();
    }

    public AccountTotalBalanceDto getTotalUserBalance(){
        Users userLoggedIn = securityService.getUserLoggedIn();

        List<Account> accounts = accountRepository.findByUser(userLoggedIn);

        BigDecimal total = accounts.stream()
                .map(account -> account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AccountTotalBalanceDto(total);
    }

    public void addAmount(UUID id, UpdateBalanceDto updateBalanceDto){
        Users userLoggedIn = securityService.getUserLoggedIn();

        Account account = searchById(id);

        if (!account.getUser().getId().equals(userLoggedIn.getId())){
            throw new AccessForbiddenException("You can't deposit to this account");
        }

        BigDecimal newAmount = account.getBalance().add(updateBalanceDto.amount());

        account.setBalance(newAmount);
        accountRepository.save(account);
    }

    public void removeAmount(UUID id, UpdateBalanceDto updateBalanceDto){
        Users userLoggedIn = securityService.getUserLoggedIn();

        Account account = searchById(id);

        if (!account.getUser().getId().equals(userLoggedIn.getId())){
            throw new AccessForbiddenException("You can't withdraw from this account");
        }

        if (account.getBalance().compareTo(updateBalanceDto.amount()) < 0){
            throw new AccessForbiddenException("Account has no sufficient amount");
        }

        BigDecimal newAmount = account.getBalance().subtract(updateBalanceDto.amount());

        account.setBalance(newAmount);
        accountRepository.save(account);
    }
}