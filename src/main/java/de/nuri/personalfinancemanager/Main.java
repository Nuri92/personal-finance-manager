package de.nuri.personalfinancemanager;

import de.nuri.personalfinancemanager.controller.ConsoleController;
import de.nuri.personalfinancemanager.repository.ExpenseRepository;
import de.nuri.personalfinancemanager.repository.JsonExpenseRepository;
import de.nuri.personalfinancemanager.service.ExpenseManager;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Path              filePath       = Path.of("data", "expenses.json");
		ExpenseRepository repository     = new JsonExpenseRepository(filePath);
		ExpenseManager    expenseManager = new ExpenseManager(repository);
		Scanner           scanner        = new Scanner(System.in);
		ConsoleController controller     = new ConsoleController(expenseManager, scanner);
		
		controller.run();
	}
}
