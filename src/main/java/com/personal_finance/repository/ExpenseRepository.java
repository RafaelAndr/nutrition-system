package com.personal_finance.repository;

import com.personal_finance.dto.expense.ExpenseRequestDto;
import com.personal_finance.entity.Account;
import com.personal_finance.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByAccount(Account account);
}
