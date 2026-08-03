package de.nuri.personalfinancemanager.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpenseTest {
	
	@Test
	void constructor_rejectsNegativeAmount() {
		assertThrows(IllegalArgumentException.class, () ->
				new Expense(
						new BigDecimal("-2.14"),
						Category.FOOD,
						LocalDate.of(2026, 8, 3),
						"Snacks",
						"Rewe",
						Currency.getInstance("EUR")
				)
		);
	}
	
	@Test
	void constructor_rejectsNullAmount() {
		assertThrows(NullPointerException.class, () ->
				new Expense(
						null,
						Category.FOOD,
						LocalDate.of(2026, 8, 3),
						"Snacks",
						"Rewe",
						Currency.getInstance("EUR")
				)
		);
	}
	
	@Test
	void constructor_rejectsNullCategory() {
		assertThrows(NullPointerException.class, () ->
				new Expense(
						new BigDecimal("2.15"),
						null,
						LocalDate.of(2026, 8, 3),
						"Snacks",
						"Rewe",
						Currency.getInstance("EUR")
				)
		);
	}
	
	@Test
	void constructor_rejectsNullDate() {
		assertThrows(NullPointerException.class, () ->
				new Expense(
						new BigDecimal("2.15"),
						Category.GROCERIES,
						null,
						"Snacks",
						"Rewe",
						Currency.getInstance("EUR")
				)
		);
	}
	
	@Test
	void constructor_rejectsNullCurrency() {
		assertThrows(NullPointerException.class, () ->
				new Expense(
						new BigDecimal("2.15"),
						Category.GROCERIES,
						LocalDate.of(2026, 8, 3),
						"Snacks",
						"Rewe",
						null
				)
		);
	}
	
	@Test
	void constructor_rejectsZeroAmount() {
		assertThrows(IllegalArgumentException.class, () ->
				new Expense(
						new BigDecimal("0"),
						Category.FOOD,
						LocalDate.of(2026, 8, 3),
						"Snacks",
						"Rewe",
						Currency.getInstance("EUR")
				)
		);
	}
	
	@Test
	void constructor_withValidData_storesProvidedValues() {
		// Arrange
		BigDecimal expectedAmount      = new BigDecimal("12.25");
		Category   expectedCategory    = Category.ENTERTAINMENT;
		LocalDate  expectedDate        = LocalDate.of(2026, 8, 3);
		String     expectedDescription = "Cinema";
		String     expectedMerchant    = "UCI";
		Currency   expectedCurrency    = Currency.getInstance("EUR");
		
		// Act
		Expense expense = new Expense(
				expectedAmount,
				expectedCategory,
				expectedDate,
				expectedDescription,
				expectedMerchant,
				expectedCurrency
		);
		
		// Assert
		assertEquals(expectedAmount, expense.getAmount());
		assertEquals(expectedCategory, expense.getCategory());
		assertEquals(expectedDate, expense.getDate());
		assertEquals(expectedDescription, expense.getDescription());
		assertEquals(expectedMerchant, expense.getMerchant());
		assertEquals(expectedCurrency, expense.getCurrency());
	}
	
}
