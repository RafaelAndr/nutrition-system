package com.personal_finance.controller;

import com.personal_finance.dto.account.AccountBalanceDto;
import com.personal_finance.dto.account.AccountResponseDto;
import com.personal_finance.dto.account.AccountRequestDto;
import com.personal_finance.dto.account.UpdateBalanceDto;
import com.personal_finance.entity.Account;
import com.personal_finance.mapper.AccountMapper;
import com.personal_finance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@RequestBody AccountRequestDto accountRequestDto){
        Account createdAccount = accountService.save(accountMapper.toEntity(accountRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(accountMapper.toDto(createdAccount));
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountResponseDto> getById(@PathVariable UUID id){
        Account account = accountService.searchById(id);
        return ResponseEntity.ok(accountMapper.toDto(account));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws AccessDeniedException {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AccountBalanceDto>> getBalanceOfAllAccounts(){
        return ResponseEntity.ok(accountService.getBalanceOfAllAccounts());
    }

    @PostMapping("/addAmount/{id}")
    public ResponseEntity<Void> addAmount(@PathVariable UUID id, @RequestBody UpdateBalanceDto updateBalanceDto){
        accountService.addAmount(id, updateBalanceDto);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/removeAmount/{id}")
    public ResponseEntity<Void> removeAmount(@PathVariable UUID id, @RequestBody UpdateBalanceDto updateBalanceDto){
        accountService.removeAmount(id, updateBalanceDto);

        return ResponseEntity.noContent().build();
    }
}
