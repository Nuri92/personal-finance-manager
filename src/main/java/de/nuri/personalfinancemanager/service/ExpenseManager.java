package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;
import de.nuri.personalfinancemanager.repository.ExpenseRepository;
import de.nuri.personalfinancemanager.repository.InMemoryExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class ExpenseManager {
	
	private       long              newExpenseId = 1;
	private final List<Expense>     expenses;
	private final ExpenseRepository repository;
	
	public ExpenseManager() {
		this(new InMemoryExpenseRepository());
	}
	
	public ExpenseManager(ExpenseRepository repository) {
		this.repository   = Objects.requireNonNull(repository, "Repository must not be null");
		this.expenses     = new ArrayList<>(repository.load());
		this.newExpenseId = determineNextExpenseId();
		
	}
	
	private long determineNextExpenseId() {
		long highestId = 0;
		for (Expense expense : expenses
		) {
			if (expense.getId() > newExpenseId) {
				highestId = expense.getId();
			}
		}
		
		return highestId + 1;
	}
	
	public Expense addExpense(Expense expense) {
		Objects.requireNonNull(expense, "Expense must not be null");
		expense.assignId(newExpenseId);
		newExpenseId++;
		expenses.add(expense);
		repository.save(expenses);
		
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
		repository.save(expenses);
	}
	
	public void deleteExpense(long id) {
		Expense expense = getExpenseById(id);
		expenses.remove(expense);
		repository.save(expenses);
	}
	
	public List<Expense> getExpensesByDate(LocalDate date) {
		Objects.requireNonNull(date, "Date must not be null");
		return filterExpense(expense -> expense.getDate().equals(date));
	}
	
	public List<Expense> getExpensesByCategory(Category category) {
		Objects.requireNonNull(category, "Category must not be null");
		return filterExpense(expense -> expense.getCategory() == category);
	}
	
	public List<Expense> getExpensesByMerchant(String merchant) {
		Objects.requireNonNull(merchant, "Merchant must not be null");
		return filterExpense(
				expense -> expense.getMerchant().equalsIgnoreCase(merchant)
		);
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
	
	
	@Override
	public String toString() {
		return expenses.toString();
	}
}