package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseManagerTest {
	
	@Test
	void addExpense_assignsIdAndStoresExpense() {
		// Arrange: Testdaten und das zu testende Objekt vorbereiten.
		ExpenseManager expenseManager = new ExpenseManager();
		Expense expense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Lunch",
				"Café Central",
				Currency.getInstance("EUR")
		);
		
		// Act: Die zu testende Methode genau einmal aufrufen.
		Expense addedExpense = expenseManager.addExpense(expense);
		
		// Assert: Das Ergebnis und den Zustand des Managers prüfen.
		assertSame(expense, addedExpense);
		assertEquals(1L, addedExpense.getId());
		assertEquals(1, expenseManager.getAllExpenses().size());
		assertSame(addedExpense, expenseManager.getAllExpenses().get(0));
	}
	
	@Test
	void addExpense_assignsIdAndStoresExpenses() {
		// Arrange: Testdaten und die zu testenden Objekte vorbereiten
		ExpenseManager expenseManager = new ExpenseManager();
		Expense firstExpense = new Expense(
				new BigDecimal("13.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Breakfast",
				"Traumkuh",
				Currency.getInstance("EUr")
		);
		Expense secondExpense = new Expense(
				new BigDecimal("13.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Breakfast",
				"Traumkuh",
				Currency.getInstance("EUr")
		);
		
		// Act: die zu testende Methode zweimal aufrufen, mit verschiedenen Daten
		Expense firstAddedExpense  = expenseManager.addExpense(firstExpense);
		Expense secondAddedExpense = expenseManager.addExpense(secondExpense);
		
		// Asserts: Das Ergebnis und den Zustand des managers prüfen
		assertSame(firstExpense, firstAddedExpense);
		assertSame(secondExpense, secondAddedExpense);
		
		assertEquals(1L, firstAddedExpense.getId());
		assertEquals(2L, secondAddedExpense.getId());
		
		assertEquals(2, expenseManager.getAllExpenses().size());
		
		assertSame(firstAddedExpense, expenseManager.getAllExpenses().get(0));
		assertSame(secondAddedExpense, expenseManager.getAllExpenses().get(1));
	}
	
	@Test
	void getExpenseById_returnsStoredExpense() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		Expense expense = new Expense(
				new BigDecimal("12.25"),
				Category.FOOD,
				LocalDate.of(2026, 8, 4),
				"Snack",
				"Rewe",
				Currency.getInstance("EUR")
		);
		expenseManager.addExpense(expense);
		
		// Act
		Expense storedExpense = expenseManager.getExpenseById(expense.getId());
		
		// Assert
		assertSame(expense, storedExpense);
	}
	
	@Test
	void getExpenseById_throwsWhenIdDoesNotExist() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();

		// Act & Assert
		assertThrows(
				NoSuchElementException.class,
				() -> expenseManager.getExpenseById(1L)
		);
	}
}
