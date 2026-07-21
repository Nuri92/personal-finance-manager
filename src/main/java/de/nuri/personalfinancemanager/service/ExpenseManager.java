package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
		List<Expense> copyOfExpenses = new ArrayList<>(expenses);
		return copyOfExpenses;
	}
	
	public Expense getExpenseById(long id) {
		if (id <= 0) {
			throw new IllegalArgumentException("Expense ID must be greater than zero");
		}
		
		for (Expense expense : expenses) {
			if (expense.getId() == id) {
				return expense;
			}
		}
		
		throw new NoSuchElementException("No Expense with ID " + id + " found.");
	}
	
	public void updateExpense(long id, Expense updatedExpense) {
		Objects.requireNonNull(updatedExpense, "Updated expense must not be null");
		Expense expense = getExpenseById(id);
		expense.updateForm(updatedExpense);
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