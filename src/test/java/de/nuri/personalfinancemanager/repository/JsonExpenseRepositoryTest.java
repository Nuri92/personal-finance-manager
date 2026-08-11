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
	
	@Test
	void saveAndLoad_returnsMultipleExpenses() {
		// Arrange
		Path                  filePath   = tempDirectory.resolve("expenses.json");
		JsonExpenseRepository repository = new JsonExpenseRepository(filePath);
		
		Expense firstExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 6),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		firstExpense.assignId(1L);
		
		Expense secondExpense = new Expense(
				new BigDecimal("49.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 5),
				"Shoes",
				"Zalando",
				Currency.getInstance("EUR")
		);
		secondExpense.assignId(2L);
		
		List<Expense> originalExpenses = List.of(
				firstExpense,
				secondExpense
		);
		
		// Act
		repository.save(originalExpenses);
		List<Expense> loadedExpenses = repository.load();
		
		// Assert
		assertEquals(2, loadedExpenses.size());
		
		Expense loadedFirstExpense  = loadedExpenses.get(0);
		Expense loadedSecondExpense = loadedExpenses.get(1);
		
		assertEquals(firstExpense.getId(), loadedFirstExpense.getId());
		assertEquals(firstExpense.getAmount(), loadedFirstExpense.getAmount());
		
		assertEquals(secondExpense.getId(), loadedSecondExpense.getId());
		assertEquals(secondExpense.getAmount(), loadedSecondExpense.getAmount());
	}
	
	@Test
	void constructor_throwsWhenFilePathIsNull() {
		assertThrows(
				NullPointerException.class,
				() -> new JsonExpenseRepository(null)
		);
	}
	
	@Test
	void save_createsMissingParentDirectories() {
		// Arrange
		Path filePath =
				tempDirectory.resolve("nested/data/expenses.json");
		
		JsonExpenseRepository repository =
				new JsonExpenseRepository(filePath);
		
		Expense expense = new Expense(
				new BigDecimal("19.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 6),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expense.assignId(1L);
		
		// Act
		repository.save(List.of(expense));
		
		// Assert
		assertTrue(Files.exists(filePath));
	}
}
