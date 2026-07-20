package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
	
	private       long          newExpenseId = 1;
	private final List<Expense> expenses     = new ArrayList<>();
	
	public void addExpense(Expense expense) {
	
	}
	
	public List<Expense> getAllExpenses() {
		return null;
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