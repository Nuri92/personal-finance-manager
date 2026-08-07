package de.nuri.personalfinancemanager.controller;

import de.nuri.personalfinancemanager.service.ExpenseManager;

import java.util.Objects;

public class ConsoleController {
	
	private final ExpenseManager expenseManager;
	
	public ConsoleController(ExpenseManager expenseManager) {
		this.expenseManager = Objects.requireNonNull(
				expenseManager,
				"Expense manager must not be null");
	}
}
