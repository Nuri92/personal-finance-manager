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

import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
