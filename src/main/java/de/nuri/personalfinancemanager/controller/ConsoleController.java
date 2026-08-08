package de.nuri.personalfinancemanager.controller;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;
import de.nuri.personalfinancemanager.service.ExpenseManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
				
				default:
					System.out.println(
							"Ungültige Eingabe, bitte erneut versuchen."
					);
			}
			
		}
	}
	
	private void addExpense() {
		//	BigDecimal amount = readAmount();
		
		// Category category = readCategory();
		
		// LocalDate date = readDate();
		
		String description = readDescription();
		
		String merchant = readMerchant();
		
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
		for (Expense expense : expenses) {
			System.out.println(expense);
		}
	}
	
	private void showMenu() {
		System.out.println("Personal Finance Manager");
		System.out.println("1. Add expense");
		System.out.println("2. Show all expenses");
		System.out.println("0. Exit");
	}
}
