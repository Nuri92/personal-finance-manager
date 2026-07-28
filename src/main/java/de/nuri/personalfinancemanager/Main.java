package de.nuri.personalfinancemanager;

import de.nuri.personalfinancemanager.model.Category;
import de.nuri.personalfinancemanager.model.Expense;
import de.nuri.personalfinancemanager.service.ExpenseManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		
		ExpenseManager manager = new ExpenseManager();
		
		manager.addExpense(new Expense(
				new BigDecimal("10.50"),
				Category.FOOD,
				LocalDate.of(2026, 1, 10),
				"Frühstück",
				"Bäckerei",
				Currency.getInstance("EUR")));
		
		manager.addExpense(new Expense(
				new BigDecimal("2.50"),
				Category.FOOD,
				LocalDate.of(2026, 7, 25),
				"Abendessesn",
				"Kartoffelsalat",
				Currency.getInstance("EUR")));
		
		manager.addExpense(new Expense(
				new BigDecimal("24.50"),
				Category.GROCERIES,
				LocalDate.of(2026, 1, 10),
				"Einkauf",
				"Lidl",
				Currency.getInstance("EUR")));
		
		manager.addExpense(new Expense(
				new BigDecimal("12.50"),
				Category.GROCERIES,
				LocalDate.of(2026, 7, 25),
				"Einkauf",
				"Rewe",
				Currency.getInstance("EUR")));
		
		// get all
		System.out.println("all expenses: ");
		List<Expense> allExpenses = manager.getAllExpenses();
		for (Expense e : allExpenses
		) {
			System.out.println(e);
		}
		
		manager.updateExpense(1, new Expense(
				new BigDecimal("15.00"),
				Category.FOOD,
				LocalDate.of(2026, 1, 10),
				"Frühstück",
				"Bäckerei",
				Currency.getInstance("EUR")));
		
		
		System.out.println("\n\nafter update: ");
		List<Expense> allExpensesAfterUpdate = manager.getAllExpenses();
		for (Expense e : allExpensesAfterUpdate
		) {
			System.out.println(e);
		}
		
		manager.deleteExpense(2);
		System.out.println("\n\ndelete second expense: ");
		List<Expense> allExpensesAfterDeletingOne = manager.getAllExpenses();
		for (Expense expense : allExpensesAfterDeletingOne
		) {
			System.out.println(expense);
		}
		
		System.out.println("\n\nExpenses by date: ");
		List<Expense> expensesByDate = manager.getExpensesByDate(LocalDate.of(2026, 1, 10));
		for (Expense expense : expensesByDate
		) {
			System.out.println(expense);
		}
		
		System.out.println("\n\nexpenses ascending after amount: ");
		List<Expense> ascendingByAmount = manager.sortByAmountDescending();
		for (Expense expense : ascendingByAmount
		) {
			System.out.println(expense);
		}
		
		System.out.println("\n\nTotal amount for single category:");
		System.out.println("Total amount for groceries: " + manager.getTotalAmountForSingleCategory(Category.GROCERIES));
		
		System.out.println("\n\nTotal amount for each category: ");
		System.out.println(manager.getTotalAmountForEachCategory());
	}
	
}
