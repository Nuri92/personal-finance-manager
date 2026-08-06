package de.nuri.personalfinancemanager.repository;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonExpenseRepositoryTest {
	@TempDir
	Path tempDirectory;
	
	@Test
	void save_createsJsonFile() throws IOException {
		// Arrange
		Path filePath = tempDirectory.resolve("expenses.json");
		
		JsonExpenseRepository repository = new JsonExpenseRepository(filePath);
		
		Expense expense = new Expense(
				new BigDecimal("19.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 6),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expense.assignId(1L);
		
		List<Expense> expenses = List.of(expense);
		
		// Act
		repository.save(expenses);
		
		// Assert
		assertTrue(Files.exists(filePath));
		assertTrue(Files.size(filePath) > 0);
	}
	
	@Test
	void load_returnsEmptyListWhenFileDoesNotExist() {
		// Arrange
		Path filePath = tempDirectory.resolve("expense.json");
		
		JsonExpenseRepository repository = new JsonExpenseRepository(filePath);
		
		// Act
		List<Expense> loadedExpenses = repository.load();
		
		// Assert
		assertTrue(loadedExpenses.isEmpty());
	}
	
	@Test
	void load_returnsPreviouslySavedExpenses() {
		// Arrange
		Path filePath = tempDirectory.resolve("expenses.json");
		
		JsonExpenseRepository repository = new JsonExpenseRepository(filePath);
		
		Expense originalExpense = new Expense(
				new BigDecimal("49.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 6),
				"Shoes",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		originalExpense.assignId(1L);
		
		List<Expense> expenses = List.of(originalExpense);
		
		// Act
		repository.save(expenses);
		List<Expense> loadedExpenses = repository.load();
		
		// Assert
		// Assert
		assertEquals(1, loadedExpenses.size());
		
		Expense loadedExpense = loadedExpenses.get(0);
		
		assertEquals(originalExpense.getId(), loadedExpense.getId());
		assertEquals(originalExpense.getAmount(), loadedExpense.getAmount());
		assertEquals(originalExpense.getCategory(), loadedExpense.getCategory());
		assertEquals(originalExpense.getDate(), loadedExpense.getDate());
		assertEquals(originalExpense.getDescription(), loadedExpense.getDescription());
		assertEquals(originalExpense.getMerchant(), loadedExpense.getMerchant());
		assertEquals(originalExpense.getCurrency(), loadedExpense.getCurrency());
	}
	
	@Test
	void load_returnsEmptyListWhenFileIsEmpty() throws IOException {
		// Arrange
		Path filePath = tempDirectory.resolve("expenses.json");
		Files.createFile(filePath);
		
		JsonExpenseRepository repository =
				new JsonExpenseRepository(filePath);
		
		// Act
		List<Expense> loadedExpenses = repository.load();
		
		// Assert
		assertTrue(loadedExpenses.isEmpty());
	}
	
	@Test
	void load_throwsWhenJsonIsInvalid() throws Exception {
		// Arrange
		Path filePath = tempDirectory.resolve("expenses.json");
		
		Files.writeString(filePath, "This is not valid JSON");
		
		JsonExpenseRepository repository = new JsonExpenseRepository(filePath);
		
		// Act & Assert
		assertThrows(ExpensePersistenceException.class, repository::load);
	}
	
	@Test
	void save_throwsWhenExpensesIsNull() {
		// Arrange
		Path filePath = tempDirectory.resolve("expenses.json");
		
		JsonExpenseRepository repository = new JsonExpenseRepository(filePath);
		
		// Act & Assert
		assertThrows(NullPointerException.class, () -> repository.save(null));
	}
}
