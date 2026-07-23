package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

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
	
	public void deleteExpense(long id) {
		Expense expense = getExpenseById(id);
		expenses.remove(expense);
	}
	
	public List<Expense> getExpensesByDate(LocalDate date) {
		Objects.requireNonNull(date, "Date must not be null");
		return filterExpense(expense -> expense.getDate().equals(date));
	}
	
	public List<Expense> getExpensesByCategory(Category category) {
		Objects.requireNonNull(category, "Category must not be null");
		return filterExpense(expense -> expense.getCategory() == category);
	}
	
	public List<Expense> getExpensesByMerchant(Category category) {
		Objects.requireNonNull(category, "Category must not be null");
		return filterExpense(expense -> expense.getMerchant().equals(category));
	}
	
	private List<Expense> filterExpense(Predicate<Expense> predicate) {
		List<Expense> matchingExpenses = new ArrayList<>();
		for (Expense expense : expenses) {
			if (predicate.test(expense)) {
				matchingExpenses.add(expense);
			}
		}
		return matchingExpenses;
	}
	
	public List<Expense> sortByAmountAscending() {
		return sortExpenses((expense1, expense2) -> expense1.getAmount().compareTo(expense2.getAmount()));
	}
	
	public List<Expense> sortByAmountDescending() {
		return sortExpenses((expense1, expense2) -> expense2.getAmount().compareTo(expense1.getAmount()));
	}
	
	public List<Expense> sortedByDateAscending() {
		return sortExpenses((expense1, expense2) -> expense1.getDate().compareTo(expense2.getDate()));
	}
	
	public List<Expense> sortedByDateDescending() {
		return sortExpenses((expense1, expense2) -> expense2.getDate().compareTo(expense1.getDate()));
	}
	
	private List<Expense> sortExpenses(Comparator<Expense> comparator) {
		List<Expense> copy = new ArrayList<>(expenses);
		copy.sort(comparator);
		return copy;
	}
	
	public BigDecimal getTotalAmount() {
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for (Expense expense : expenses) {
			totalAmount = totalAmount.add(expense.getAmount());
		}
		
		return totalAmount;
	}
	
	public BigDecimal getTotalAmountByCategory(Category category) {
		Objects.requireNonNull(category, "Category must not be null");
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for (Expense expense : expenses) {
			if (expense.getCategory() == category) {
				totalAmount = totalAmount.add(expense.getAmount());
			}
		}
		
		return totalAmount;
	}
	
	public Map<Category, BigDecimal> getTotalAmountForEachCategory() {
		Map<Category, BigDecimal> totalAmounts = new HashMap<>();
		
		for (Expense expense : expenses) {
			Category   category     = expense.getCategory();
			BigDecimal currentTotal = totalAmounts.get(category);
			
			if (currentTotal != null) {
				totalAmounts.put(
						category,
						currentTotal.add(expense.getAmount())
				);
			} else {
				totalAmounts.put(category, expense.getAmount());
			}
		}
		
		return totalAmounts;
	}
	
	public BigDecimal getTotalAmountForSingleCategory(Category category) {
		Objects.requireNonNull(category, "Category must not be null");
		
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for (Expense expense : expenses) {
			if (expense.getCategory() == category) {
				totalAmount = totalAmount.add(expense.getAmount());
			}
		}
		
		return totalAmount;
	}
}