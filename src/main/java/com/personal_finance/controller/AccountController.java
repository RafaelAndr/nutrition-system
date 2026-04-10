package com.personal_finance.controller;

import com.personal_finance.dto.account.*;
import com.personal_finance.entity.Account;
import com.personal_finance.mapper.AccountMapper;
import com.personal_finance.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Account", description = "It contains all operations related to registering, editing, and reading account information.")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping
    @Operation(summary = "Save", description = "Register new account")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Account successfully registered.",
                    content = @Content(schema = @Schema(implementation = AccountResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "409", description = "account already registered"),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account data to create",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccountRequestDto.class)
            )
    )
    public ResponseEntity<AccountResponseDto> create(@RequestBody AccountRequestDto accountRequestDto){
        Account createdAccount = accountService.save(accountMapper.toEntity(accountRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(accountMapper.toDto(createdAccount));
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Get account by ID",
            description = "Retrieve detailed information of an account by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Account retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found"
            )
    })
    public ResponseEntity<AccountResponseDto> getById(
            @Parameter(description = "Account ID", required = true)
            @PathVariable UUID id){

        Account account = accountService.searchById(id);
        return ResponseEntity.ok(accountMapper.toDto(account));
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> editBankName(@PathVariable UUID id, @RequestBody AccountRequestDto accountRequestDto){
        accountService.editBankName(id, accountRequestDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws AccessDeniedException {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AccountBalanceDto>> getUserAccounts(){
        return ResponseEntity.ok(accountService.getUserAccounts());
    }

    @GetMapping("/total-balance")
    public ResponseEntity<AccountTotalBalanceDto> getTotalUserBalance(){
        return ResponseEntity.ok(accountService.getTotalUserBalance());
    }

    @PostMapping("/add-amount/{id}")
    public ResponseEntity<Void> addAmount(@PathVariable UUID id, @RequestBody UpdateBalanceDto updateBalanceDto){
        accountService.addAmount(id, updateBalanceDto);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/remove-amount/{id}")
    public ResponseEntity<Void> removeAmount(@PathVariable UUID id, @RequestBody UpdateBalanceDto updateBalanceDto){
        accountService.removeAmount(id, updateBalanceDto);

        return ResponseEntity.noContent().build();
    }
}
