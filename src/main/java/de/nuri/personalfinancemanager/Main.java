package de.nuri.personalfinancemanager;

import de.nuri.personalfinancemanager.repository.ExpenseRepository;
import de.nuri.personalfinancemanager.repository.JsonExpenseRepository;
import de.nuri.personalfinancemanager.service.ExpenseManager;

import java.nio.file.Path;

public class Main {
	public static void main(String[] args) {
		Path              filePath       = Path.of("data", "expenses.json");
		ExpenseRepository repository     = new JsonExpenseRepository(filePath);
		ExpenseManager    expenseManager = new ExpenseManager(repository);
	}
}
