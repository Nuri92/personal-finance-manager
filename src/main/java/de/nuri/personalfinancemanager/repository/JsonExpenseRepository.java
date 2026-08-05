package de.nuri.personalfinancemanager.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.nuri.personalfinancemanager.model.Expense;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class JsonExpenseRepository implements ExpenseRepository {
	
	private final Path         filePath;
	private final ObjectMapper objectMapper;
	
	public JsonExpenseRepository(Path filePath) {
		this.filePath = Objects.requireNonNull(
				filePath,
				"File path must not be null");
		
		this.objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}
	
	@Override
	public void save(List<Expense> expenses) {
		Objects.requireNonNull(expenses, "Expenses must not be null");
		
		try {
			Path parentDirectory = filePath.getParent();
			
			if (parentDirectory != null) {
				Files.createDirectories(parentDirectory);
			}
			
			objectMapper
					.writerWithDefaultPrettyPrinter()
					.writeValue(filePath.toFile(), expenses);
			
		} catch (IOException exception) {
			throw new ExpensePersistenceException("Could not save expense to " + filePath, exception);
		}
	}
	
	@Override
	public List<Expense> load() {
		return null;
	}
}
