package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpenseManager {
	
	private       long          newExpenseId = 1;
	private final List<Expense> expenses     = new ArrayList<>();
	
	public Expense addExpense(Expense expense) {
		Objects.requireNonNull(expense, "Expense must not be null");
		expense.assignId(newExpenseId);
		newExpenseId++;
		expenses.add(expense);
		
		return expense;
	}
	
	public List<Expense> getAllExpenses() {
		return expenses;
	}
	
	public Expense getExpenseById(long expenseId) {
		return null;
	}
	
	public void updateExpense(long expenseId, Expense updatedExpense) {
	
	}
	
	public void deleteExpense(long expenseId) {
	
	}
	
	public List<Expense> getExpensesSortedByDate() {
		return null;
	}
	
	public List<Expense> getExpensesByCategory(Category category) {
		return null;
	}
}