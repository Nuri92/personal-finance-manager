package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;
import de.nuri.personalfinancemanager.repository.JsonExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ExpenseJsonIntegrationTest {
	@TempDir
	Path tempDirectory;
	
	@Test
	void saveAndLoad_withTwoExpenseManagers() {
		// Arrange
		Path filePath = tempDirectory.resolve("expenses.json");
		
		JsonExpenseRepository firstRepository =
				new JsonExpenseRepository(filePath);
		
		ExpenseManager firstManager =
				new ExpenseManager(firstRepository);
		
		Expense firstExpense = new Expense(
				new BigDecimal("12.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 1, 8),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense secondExpense = new Expense(
				new BigDecimal("14.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 1, 8),
				"Jacket",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		firstManager.addExpense(firstExpense);
		firstManager.addExpense(secondExpense);
		
		// Simulierter Neustart
		JsonExpenseRepository secondRepository =
				new JsonExpenseRepository(filePath);
		
		ExpenseManager secondManager =
				new ExpenseManager(secondRepository);
		
		List<Expense> initiallyLoadedExpenses =
				secondManager.getAllExpenses();
		
		Expense thirdExpense = new Expense(
				new BigDecimal("19.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 1, 8),
				"Jeans",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		// Act
		secondManager.addExpense(thirdExpense);
		
		List<Expense> expensesAfterAdding =
				secondManager.getAllExpenses();
		
		// Assert
		assertEquals(2, initiallyLoadedExpenses.size());
		assertEquals(3, expensesAfterAdding.size());
		assertEquals(3L, thirdExpense.getId());
	}
}
