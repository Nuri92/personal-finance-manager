package de.nuri.personalfinancemanager.controller;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;
import de.nuri.personalfinancemanager.service.ExpenseManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ConsoleController {
	
	private final ExpenseManager expenseManager;
	private final Scanner        scanner;
	
	public ConsoleController(ExpenseManager expenseManager, Scanner scanner) {
		this.expenseManager = Objects.requireNonNull(
				expenseManager,
				"Expense manager must not be null");
		
		this.scanner = Objects.requireNonNull(scanner,
				"Scanner must not be null");
	}
	
	public void run() {
		boolean running = true;
		
		while (running) {
			showMenu();
			System.out.println("Tätigen Sie ihre Eingabe: ");
			String input = scanner.nextLine();
			int    userInput;
			
			try {
				userInput = Integer.parseInt(input);
			} catch (NumberFormatException exception) {
				System.out.println("Bitte geben Sie eine gültige Zahl ein.");
				continue;
			}
			
			switch (userInput) {
				case 0:
					running = false;
					break;
				
				case 1:
					addExpense();
					break;
				
				case 2:
					showAllExpenses();
					break;
				
				case 3:
					deleteExpense();
					break;
				case 4:
					updateExpense();
					break;
				case 5:
					filterByCategory();
					break;
				case 6:
					getTotalAmount();
					break;
				case 7:
					getTotalAmountByCategory();
					break;
				case 8:
					sortByAmountAscending();
					break;
				case 9:
					sortByAmountDescending();
					break;
				case 10:
					sortByDateAscending();
					break;
				case 11:
					sortByDateDescending();
					break;
				default:
					System.out.println(
							"Ungültige Eingabe, bitte erneut versuchen."
					);
			}
			
		}
	}
	
	private void sortByDateDescending() {
		List<Expense> sortedExpenses = expenseManager.sortedByDateDescending();
		printExpenses(sortedExpenses);
	}
	
	private void sortByDateAscending() {
		List<Expense> sortedExpenses = expenseManager.sortedByDateAscending();
		printExpenses(sortedExpenses);
	}
	
	private void sortByAmountDescending() {
		List<Expense> sortedList = expenseManager.sortByAmountDescending();
		printExpenses(sortedList);
	}
	
	private void sortByAmountAscending() {
		List<Expense> sortedList = expenseManager.sortByAmountAscending();
		printExpenses(sortedList);
		
	}
	
	private void printExpenses(List<Expense> expenses) {
		for (Expense expense : expenses) {
			System.out.println(expense);
		}
	}
	
	private void getTotalAmountByCategory() {
		System.out.println("Choose a category to get the total amount: ");
		Category   category    = readCategory();
		BigDecimal totalAmount = expenseManager.getTotalAmountByCategory(category);
		System.out.println("Total amount for " + category + ": " + totalAmount);
	}
	
	private void getTotalAmount() {
		BigDecimal totalAmount = expenseManager.getTotalAmount();
		System.out.println("Total Amount: " + totalAmount);
	}
	
	private void filterByCategory() {
		Category category = readCategory();
		
		List<Expense> filteredList =
				expenseManager.getExpensesByCategory(category);
		
		if (filteredList.isEmpty()) {
			System.out.println(
					"No expenses found for category: " + category
			);
			return;
		}
		
		System.out.println("Expenses for category: " + category);
		
		printExpenses(filteredList);
	}
	
	private void updateExpense() {
		while (true) {
			System.out.println("Update Expense:");
			System.out.println("Enter 0 to exit or enter an ID to update an Expense:");
			
			String input = scanner.nextLine();
			
			if (input.equals("0")) {
				return;
			}
			
			try {
				long id = Long.parseLong(input);
				
				if (id <= 0) {
					System.out.println("Expense ID must be greater than zero.");
					continue;
				}
				
				try {
					expenseManager.getExpenseById(id);
					Expense updatedExpense = createExpense();
					expenseManager.updateExpense(id, updatedExpense);
					System.out.println("Expense with ID " + id + " successfully updated.");
					
					return;
					
				} catch (NoSuchElementException exception) {
					System.out.println("No expense found with ID: " + id + ". Please try again.");
				}
				
			} catch (NumberFormatException exception) {
				System.out.println("Please enter a valid ID.");
			}
		}
	}
	
	private Expense createExpense() {
		BigDecimal amount      = readAmount();
		Category   category    = readCategory();
		LocalDate  date        = readDate();
		String     description = readDescription();
		String     merchant    = readMerchant();
		Currency   currency    = readCurrency();
		
		Expense expense = new Expense(
				amount,
				category,
				date,
				description,
				merchant,
				currency
		);
		
		return expense;
	}
	
	private void deleteExpense() {
		while (true) {
			System.out.println("Type in the Id of the expense u want to delete: ");
			System.out.println("Or type in 0 to get to the menu: ");
			String input = scanner.nextLine();
			if (input.equals("0")) {
				System.out.println("Back to Main menu.");
				return;
			}
			try {
				long id = Long.parseLong(input);
				try {
					expenseManager.deleteExpense(id);
					System.out.println("Expense with id " + id + " successfully deleted.");
				} catch (NoSuchElementException exception) {
					System.out.println("No expense with id: " + id);
				}
				
			} catch (NumberFormatException exception) {
				System.out.println("Type in valid value");
			}
		}
	}
	
	private void addExpense() {
		Expense expense      = createExpense();
		Expense addedExpense = expenseManager.addExpense(expense);
		System.out.println("Expense successfully added with ID: " + addedExpense.getId());
	}
	
	private Currency readCurrency() {
		while (true) {
			System.out.println("Choose a currency:");
			System.out.println("Type 1 for Euro (EUR)");
			System.out.println("Type 2 for US Dollar (USD)");
			System.out.println("Type 3 for British Pound (GBP)");
			
			String input = scanner.nextLine();
			
			switch (input) {
				case "1":
					return Currency.getInstance("EUR");
				
				case "2":
					return Currency.getInstance("USD");
				
				case "3":
					return Currency.getInstance("GBP");
				
				default:
					System.out.println("Please enter a valid value.");
			}
		}
	}
	
	private String readMerchant() {
		System.out.println("Merchant: ");
		return scanner.nextLine();
	}
	
	private String readDescription() {
		System.out.println("Description: ");
		return scanner.nextLine();
	}
	
	private LocalDate readDate() {
		while (true) {
			System.out.println("Date (yyyy-MM-dd): ");
			String input = scanner.nextLine();
			try {
				return LocalDate.parse(input);
			} catch (DateTimeParseException exception) {
				System.out.println("Please enter valid date.");
			}
		}
	}
	
	private Category readCategory() {
		while (true) {
			System.out.println("Choose a category: ");
			System.out.println("Type 1 for food: ");
			System.out.println("Type 2 for groceries: ");
			System.out.println("Type 3 for rent: ");
			System.out.println("Type 4 for transport: ");
			System.out.println("Type 5 for health: ");
			System.out.println("Type 6 for shopping: ");
			System.out.println("Type 7 for subscription: ");
			System.out.println("Type 8 for entertainment: ");
			System.out.println("Type 9 for other: ");
			String input = scanner.nextLine();
			
			try {
				int userInput = Integer.parseInt(input);
				switch (userInput) {
					case 1:
						return Category.FOOD;
					case 2:
						return Category.GROCERIES;
					case 3:
						return Category.RENT;
					case 4:
						return Category.TRANSPORT;
					case 5:
						return Category.HEALTH;
					case 6:
						return Category.SHOPPING;
					case 7:
						return Category.SUBSCRIPTIONS;
					case 8:
						return Category.ENTERTAINMENT;
					case 9:
						return Category.OTHER;
					default:
						System.out.println("Please choose a number between 1 and 9.");
				}
			} catch (NumberFormatException exception) {
				System.out.println("Please enter valid value");
			}
			
		}
	}
	
	private BigDecimal readAmount() {
		while (true) {
			System.out.println("Amount: ");
			String amountInput = scanner.nextLine();
			try {
				return new BigDecimal(amountInput);
			} catch (NumberFormatException exception) {
				System.out.println("Please enter valid value.");
			}
		}
	}
	
	private void showAllExpenses() {
		List<Expense> expenses = expenseManager.getAllExpenses();
		System.out.println("Loaded expenses: " + expenses.size());
		printExpenses(expenses);
	}
	
	
	private void showMenu() {
		System.out.println("Personal Finance Manager");
		System.out.println("1. Add expense");
		System.out.println("2. Show all expenses");
		System.out.println("3. Delete an Expense");
		System.out.println("4. Update an Expense");
		System.out.println("5. Filter expense by category");
		System.out.println("6. Get total amount");
		System.out.println("7. Get total amount by category");
		System.out.println("8. sort by amount ascending");
		System.out.println("9. sort by amount descending");
		System.out.println("10. sort by date ascending");
		System.out.println("11. sort by date descending");
		System.out.println("0. Exit");
	}
}
