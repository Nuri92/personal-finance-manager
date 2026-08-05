package de.nuri.personalfinancemanager.repository;

import de.nuri.personalfinancemanager.model.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryExpenseRepository implements ExpenseRepository {
	
	private List<Expense> storedExpenses = new ArrayList<>();
	
	@Override
	public void save(List<Expense> expenses) {
		Objects.requireNonNull(expenses, "Expenses must not be null");
		storedExpenses = new ArrayList<>(expenses);
	}
	
	@Override
	public List<Expense> load() {
		return new ArrayList<>(storedExpenses);
	}
}
