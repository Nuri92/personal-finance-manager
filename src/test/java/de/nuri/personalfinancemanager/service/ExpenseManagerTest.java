package de.nuri.personalfinancemanager.service;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;
import de.nuri.personalfinancemanager.repository.InMemoryExpenseRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
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
	void addExpense_usesHighestExistingIdPlusOne() {
		// Arrange
		InMemoryExpenseRepository repository =
				new InMemoryExpenseRepository();
		
		Expense firstExpense = new Expense(
				new BigDecimal("13.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Breakfast",
				"Traumkuh",
				Currency.getInstance("EUR")
		);
		firstExpense.assignId(2L);
		
		Expense secondExpense = new Expense(
				new BigDecimal("20.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 4),
				"Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		secondExpense.assignId(7L);
		
		Expense thirdExpense = new Expense(
				new BigDecimal("8.50"),
				Category.TRANSPORT,
				LocalDate.of(2026, 8, 5),
				"Ticket",
				"HVV",
				Currency.getInstance("EUR")
		);
		thirdExpense.assignId(4L);
		
		repository.save(
				List.of(firstExpense, secondExpense, thirdExpense)
		);
		
		ExpenseManager expenseManager =
				new ExpenseManager(repository);
		
		Expense fourthExpense = new Expense(
				new BigDecimal("9.99"),
				Category.ENTERTAINMENT,
				LocalDate.of(2026, 8, 6),
				"Cinema",
				"UCI",
				Currency.getInstance("EUR")
		);
		
		// Act
		Expense addedExpense =
				expenseManager.addExpense(fourthExpense);
		
		// Assert
		assertEquals(8L, addedExpense.getId());
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
				Currency.getInstance("EUR")
		);
		Expense secondExpense = new Expense(
				new BigDecimal("13.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Breakfast",
				"Traumkuh",
				Currency.getInstance("EUR")
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
	
	@Test
	void getExpenseById_throwsWhenIdIsZero() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		
		// Act & Assert
		assertThrows(
				IllegalArgumentException.class,
				() -> expenseManager.getExpenseById(0L)
		);
	}
	
	@Test
	void getExpenseById_throwsWhenIdIsNegative() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		
		// Act & Assert
		assertThrows(
				IllegalArgumentException.class,
				() -> expenseManager.getExpenseById(-1L)
		);
	}
	
	@Test
	void deleteExpense_removesStoredExpense() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		
		Expense expense = new Expense(
				new BigDecimal("112.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 1),
				"Jacket",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expenseManager.addExpense(expense);
		
		// Act
		expenseManager.deleteExpense(expense.getId());
		
		// Assert
		assertTrue(expenseManager.getAllExpenses().isEmpty());
	}
	
	@Test
	void deleteExpense_throwsWhenIdDoesNotExist() {
		ExpenseManager expenseManager = new ExpenseManager();
		assertThrows(NoSuchElementException.class, () -> {
			expenseManager.deleteExpense(4L);
		});
	}
	
	@Test
	void deleteExpense_throwsWhenIdIsZero() {
		ExpenseManager expenseManager = new ExpenseManager();
		assertThrows(IllegalArgumentException.class, () -> {
			expenseManager.deleteExpense(0L);
		});
	}
	
	@Test
	void deleteExpense_throwsWhenIdIsNegative() {
		ExpenseManager expenseManager = new ExpenseManager();
		assertThrows(IllegalArgumentException.class, () -> {
			expenseManager.deleteExpense(-4L);
		});
	}
	
	@Test
	void updateExpense_updatesStoredExpenseValues() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		Expense originalExpense = new Expense(
				new BigDecimal("23.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 4),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		Expense updatedExpense = new Expense(
				new BigDecimal("19.99"),
				Category.ENTERTAINMENT,
				LocalDate.of(2026, 8, 3),
				"Cinema ticket",
				"UCI",
				Currency.getInstance("USD")
		);
		expenseManager.addExpense(originalExpense);
		long originalId = originalExpense.getId();
		
		// Act
		expenseManager.updateExpense(originalId, updatedExpense);
		Expense storedExpense = expenseManager.getExpenseById(originalId);
		
		// Assert
		assertSame(originalExpense, storedExpense);
		assertEquals(originalId, storedExpense.getId());
		assertEquals(updatedExpense.getAmount(), storedExpense.getAmount());
		assertEquals(updatedExpense.getCategory(), storedExpense.getCategory());
		assertEquals(updatedExpense.getDate(), storedExpense.getDate());
		assertEquals(updatedExpense.getDescription(), storedExpense.getDescription());
		assertEquals(updatedExpense.getMerchant(), storedExpense.getMerchant());
		assertEquals(updatedExpense.getCurrency(), storedExpense.getCurrency());
	}
	
	@Test
	void updateExpense_throwsWhenUpdatedExpenseIsNull() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		
		// Act & Assert
		assertThrows(NullPointerException.class, () -> {
			expenseManager.updateExpense(1L, null);
		});
	}
	
	@Test
	void updateExpense_throwsWhenIdDoesNotExist() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		Expense updatedExpense = new Expense(
				new BigDecimal("12.25"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 2),
				"Jeans",
				"H&M",
				Currency.getInstance("EUR")
		);
		
		// Act & Assert
		assertThrows(
				NoSuchElementException.class,
				() -> expenseManager.updateExpense(1L, updatedExpense)
		);
	}
	
	@Test
	void getAllExpenses_returnsDefensiveCopy() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		Expense expense = new Expense(new BigDecimal("0.79"),
				Category.FOOD,
				LocalDate.of(2026, 8, 1),
				"Wasser",
				"Rewe",
				Currency.getInstance("EUR"));
		
		expenseManager.addExpense(expense);
		
		// Act
		List<Expense> returnedExpenses = expenseManager.getAllExpenses();
		returnedExpenses.clear();
		
		// Assert
		assertEquals(1, expenseManager.getAllExpenses().size());
	}
	
	@Test
	void addExpense_continuesIdAfterLoadingStoredExpenses() {
		// Arrange
		InMemoryExpenseRepository repository = new InMemoryExpenseRepository();
		
		ExpenseManager firstManager = new ExpenseManager(repository);
		
		Expense firstExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense secondExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 5),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		
		firstManager.addExpense(firstExpense);
		firstManager.addExpense(secondExpense);
		
		ExpenseManager secondManager = new ExpenseManager(repository);
		
		Expense thirdExpense = new Expense(
				new BigDecimal("9.99"),
				Category.ENTERTAINMENT,
				LocalDate.of(2026, 8, 5),
				"Cinema",
				"UCI",
				Currency.getInstance("EUR")
		);
		
		// Act
		Expense addedExpense = secondManager.addExpense(thirdExpense);
		
		// Assert
		assertEquals(3L, addedExpense.getId());
		assertEquals(3, secondManager.getAllExpenses().size());
	}
	
	@Test
	void getExpensesByCategory() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		Expense firstExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense secondExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 5),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense thirdExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense fourthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 5),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense fifthExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense sixthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 5),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expenseManager.addExpense(firstExpense);
		expenseManager.addExpense(secondExpense);
		expenseManager.addExpense(thirdExpense);
		expenseManager.addExpense(fourthExpense);
		expenseManager.addExpense(fifthExpense);
		expenseManager.addExpense(sixthExpense);
		
		// Act
		List<Expense> expenses = expenseManager.getExpensesByCategory(Category.FOOD);
		
		// Assert
		assertEquals(3, expenses.size());
		
		for (Expense expense : expenses) {
			assertEquals(Category.FOOD, expense.getCategory());
		}
	}
	
	@Test
	void getExpensesByDate() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		LocalDate      targetDate     = LocalDate.of(2026, 8, 3);
		Expense firstExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense secondExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense thirdExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense fourthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense fifthExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense sixthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expenseManager.addExpense(firstExpense);
		expenseManager.addExpense(secondExpense);
		expenseManager.addExpense(thirdExpense);
		expenseManager.addExpense(fourthExpense);
		expenseManager.addExpense(fifthExpense);
		expenseManager.addExpense(sixthExpense);
		
		// Act
		List<Expense> expenses = expenseManager.getExpensesByDate(targetDate);
		
		// Assert
		assertEquals(4, expenses.size());
		
		for (Expense expense : expenses) {
			assertEquals(targetDate, expense.getDate());
		}
	}
	
	@Test
	void getExpensesByMerchant() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		String         targetMerchant = "ReWe";
		Expense firstExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense secondExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense thirdExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense fourthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense fifthExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense sixthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expenseManager.addExpense(firstExpense);
		expenseManager.addExpense(secondExpense);
		expenseManager.addExpense(thirdExpense);
		expenseManager.addExpense(fourthExpense);
		expenseManager.addExpense(fifthExpense);
		expenseManager.addExpense(sixthExpense);
		
		// Act
		List<Expense> expenses = expenseManager.getExpensesByMerchant(targetMerchant);
		
		// Assert
		assertEquals(3, expenses.size());
		
		for (Expense expense : expenses) {
			assertEquals("Rewe", expense.getMerchant());
		}
	}
	
	@Test
	void sortExpensesByAmountAscending() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		Expense firstExpense = new Expense(
				new BigDecimal("120.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense secondExpense = new Expense(
				new BigDecimal("250.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense thirdExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense fourthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense fifthExpense = new Expense(
				new BigDecimal("102.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense sixthExpense = new Expense(
				new BigDecimal("205.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expenseManager.addExpense(firstExpense);
		expenseManager.addExpense(secondExpense);
		expenseManager.addExpense(thirdExpense);
		expenseManager.addExpense(fourthExpense);
		expenseManager.addExpense(fifthExpense);
		expenseManager.addExpense(sixthExpense);
		
		// Act
		List<Expense> sortedList = expenseManager.sortByAmountAscending();
		
		// Assert
		assertEquals(6, sortedList.size());
		assertEquals(thirdExpense.getAmount(), sortedList.get(0).getAmount());
		assertEquals(fourthExpense.getAmount(), sortedList.get(1).getAmount());
		assertEquals(fifthExpense.getAmount(), sortedList.get(2).getAmount());
		assertEquals(firstExpense.getAmount(), sortedList.get(3).getAmount());
		assertEquals(sixthExpense.getAmount(), sortedList.get(4).getAmount());
		assertEquals(secondExpense.getAmount(), sortedList.get(5).getAmount());
	}
	
	@Test
	void sortExpensesByAmountDescending() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		Expense firstExpense = new Expense(
				new BigDecimal("120.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense secondExpense = new Expense(
				new BigDecimal("250.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense thirdExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense fourthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense fifthExpense = new Expense(
				new BigDecimal("102.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense sixthExpense = new Expense(
				new BigDecimal("205.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 3),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expenseManager.addExpense(firstExpense);
		expenseManager.addExpense(secondExpense);
		expenseManager.addExpense(thirdExpense);
		expenseManager.addExpense(fourthExpense);
		expenseManager.addExpense(fifthExpense);
		expenseManager.addExpense(sixthExpense);
		
		// Act
		List<Expense> sortedList = expenseManager.sortByAmountDescending();
		
		// Assert
		assertEquals(6, sortedList.size());
		assertEquals(secondExpense.getAmount(), sortedList.get(0).getAmount());
		assertEquals(sixthExpense.getAmount(), sortedList.get(1).getAmount());
		assertEquals(firstExpense.getAmount(), sortedList.get(2).getAmount());
		assertEquals(fifthExpense.getAmount(), sortedList.get(3).getAmount());
		assertEquals(fourthExpense.getAmount(), sortedList.get(4).getAmount());
		assertEquals(thirdExpense.getAmount(), sortedList.get(5).getAmount());
	}
	
	@Test
	void sortExpensesByDateAscending() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		Expense firstExpense = new Expense(
				new BigDecimal("120.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense secondExpense = new Expense(
				new BigDecimal("250.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 4),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense thirdExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense fourthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 2),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense fifthExpense = new Expense(
				new BigDecimal("102.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 1),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense sixthExpense = new Expense(
				new BigDecimal("205.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 6),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expenseManager.addExpense(firstExpense);
		expenseManager.addExpense(secondExpense);
		expenseManager.addExpense(thirdExpense);
		expenseManager.addExpense(fourthExpense);
		expenseManager.addExpense(fifthExpense);
		expenseManager.addExpense(sixthExpense);
		
		// Act
		List<Expense> sortedList = expenseManager.sortByDateAscending();
		
		// Assert
		assertEquals(6, sortedList.size());
		assertEquals(fifthExpense.getDate(), sortedList.get(0).getDate());
		assertEquals(fourthExpense.getDate(), sortedList.get(1).getDate());
		assertEquals(thirdExpense.getDate(), sortedList.get(2).getDate());
		assertEquals(secondExpense.getDate(), sortedList.get(3).getDate());
		assertEquals(firstExpense.getDate(), sortedList.get(4).getDate());
		assertEquals(sixthExpense.getDate(), sortedList.get(5).getDate());
	}
	
	@Test
	void sortExpensesByDateDescending() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		Expense firstExpense = new Expense(
				new BigDecimal("120.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense secondExpense = new Expense(
				new BigDecimal("250.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 4),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense thirdExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense fourthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 2),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		Expense fifthExpense = new Expense(
				new BigDecimal("102.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 1),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		
		Expense sixthExpense = new Expense(
				new BigDecimal("205.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 6),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		expenseManager.addExpense(firstExpense);
		expenseManager.addExpense(secondExpense);
		expenseManager.addExpense(thirdExpense);
		expenseManager.addExpense(fourthExpense);
		expenseManager.addExpense(fifthExpense);
		expenseManager.addExpense(sixthExpense);
		
		// Act
		List<Expense> sortedList = expenseManager.sortByDateDescending();
		
		// Assert
		assertEquals(6, sortedList.size());
		assertEquals(sixthExpense.getDate(), sortedList.get(0).getDate());
		assertEquals(firstExpense.getDate(), sortedList.get(1).getDate());
		assertEquals(secondExpense.getDate(), sortedList.get(2).getDate());
		assertEquals(thirdExpense.getDate(), sortedList.get(3).getDate());
		assertEquals(fourthExpense.getDate(), sortedList.get(4).getDate());
		assertEquals(fifthExpense.getDate(), sortedList.get(5).getDate());
	}
	
	@Test
	void getTotalAmount() {
		// Arrange
		ExpenseManager expenseManager = new ExpenseManager();
		BigDecimal     expectedAmount = BigDecimal.ZERO;
		Expense firstExpense = new Expense(
				new BigDecimal("120.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 5),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		expectedAmount = expectedAmount.add(firstExpense.getAmount());
		
		Expense secondExpense = new Expense(
				new BigDecimal("250.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 4),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		expectedAmount = expectedAmount.add(secondExpense.getAmount());
		
		Expense thirdExpense = new Expense(
				new BigDecimal("12.50"),
				Category.FOOD,
				LocalDate.of(2026, 8, 3),
				"Lunch",
				"Rewe",
				Currency.getInstance("EUR")
		);
		expectedAmount = expectedAmount.add(thirdExpense.getAmount());
		
		
		Expense fourthExpense = new Expense(
				new BigDecimal("25.00"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 2),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		expectedAmount = expectedAmount.add(fourthExpense.getAmount());
		
		
		expenseManager.addExpense(firstExpense);
		expenseManager.addExpense(secondExpense);
		expenseManager.addExpense(thirdExpense);
		expenseManager.addExpense(fourthExpense);
		
		// Act
		BigDecimal totalAmount = expenseManager.getTotalAmount();
		
		// Assert
		assertEquals(expectedAmount, totalAmount);
	}
	
	@Test
	void getTotalAmount_returnsZeroWhenNoExpensesExist() {
		ExpenseManager expenseManager = new ExpenseManager();
		
		BigDecimal totalAmount = expenseManager.getTotalAmount();
		
		assertEquals(BigDecimal.ZERO, totalAmount);
	}
}
