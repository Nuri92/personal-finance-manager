package de.nuri.personalfinancemanager.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseJsonTest {
	
	@Test
	void expense_canBeSerializedAndDeserialized() throws Exception {
		// Arrange
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		
		Expense originalExpense = new Expense(
				new BigDecimal("19.99"),
				Category.SHOPPING,
				LocalDate.of(2026, 8, 5),
				"T-Shirt",
				"Zalando",
				Currency.getInstance("EUR")
		);
		
		originalExpense.assignId(3L);
		
		// Act
		String  json          = objectMapper.writeValueAsString(originalExpense);
		Expense loadedExpense = objectMapper.readValue(json, Expense.class);
		
		// Assert
		assertEquals(originalExpense.getId(), loadedExpense.getId());
		assertEquals(originalExpense.getAmount(), loadedExpense.getAmount());
		assertEquals(originalExpense.getCategory(), loadedExpense.getCategory());
		assertEquals(originalExpense.getDate(), loadedExpense.getDate());
		assertEquals(originalExpense.getDescription(), loadedExpense.getDescription());
		assertEquals(originalExpense.getMerchant(), loadedExpense.getMerchant());
		assertEquals(originalExpense.getCurrency(), loadedExpense.getCurrency());
	}
}
