package de.nuri.personalfinancemanager.repository;

import de.nuri.personalfinancemanager.model.Expense;

import java.util.List;

public interface ExpenseRepository {
	void save(List<Expense> expenses);
	
	List<Expense> load();
}
