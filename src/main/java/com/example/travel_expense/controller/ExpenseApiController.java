package com.example.travel_expense.controller;

import com.example.travel_expense.entity.Expense;
import com.example.travel_expense.repository.ExpenseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseApiController {

    private final ExpenseRepository expenseRepository;

    public ExpenseApiController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
}
