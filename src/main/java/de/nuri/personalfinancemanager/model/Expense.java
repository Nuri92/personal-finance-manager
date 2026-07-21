package de.nuri.personalfinancemanager.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;

public class Expense {
	private long       id;
	private BigDecimal amount;
	private Category   category;
	private LocalDate  date;
	private String     description;
	private String     merchant;
	private Currency   currency;
	
	public Expense(BigDecimal amount, Category category, LocalDate date) {
		this.amount      = validateAmount(amount);
		this.category    = Objects.requireNonNull(category, "Category must not be null");
		this.date        = Objects.requireNonNull(date, "Date must not be null");
		this.description = "";
		this.merchant    = "";
		this.currency    = Currency.getInstance("EUR");
	}
	
	public long getId() {
		return id;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getMerchant() {
		return merchant;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void assignId(long id) {
		if (id <= 0) {
			throw new IllegalArgumentException("Expense ID must be greater than zero");
		}
		
		if (this.id != 0) {
			throw new IllegalStateException("Expense already has an ID");
		}
		
		this.id = id;
	}
	
	public void updateForm(Expense updatedExpense) {
		Objects.requireNonNull(updatedExpense, "Updated expense must not be null");
		changeAmount(updatedExpense.getAmount());
		changeCategory(updatedExpense.getCategory());
		changeDate(updatedExpense.getDate());
		changeCurrency(updatedExpense.getCurrency());
		changeDescription(updatedExpense.getDescription());
		changeMerchant(updatedExpense.getMerchant());
	}
	
	public void changeAmount(BigDecimal amount) {
		this.amount = validateAmount(amount);
	}
	
	public void changeCategory(Category newCategory) {
		this.category = Objects.requireNonNull(newCategory, "Category must not be null");
	}
	
	public void changeDate(LocalDate newDate) {
		this.date = Objects.requireNonNull(newDate, "Date must not be null");
	}
	
	public void changeDescription(String newDescription) {
		this.description = normalizeOptionalText(newDescription);
	}
	
	public void changeMerchant(String newMerchant) {
		this.merchant = normalizeOptionalText(newMerchant);
	}
	
	public void changeCurrency(Currency newCurrency) {
		this.currency = Objects.requireNonNull(newCurrency, "Currency must not be null");
	}
	
	private static BigDecimal validateAmount(BigDecimal amount) {
		Objects.requireNonNull(amount, "Amount must not be null");
		
		if (amount.signum() <= 0) {
			throw new IllegalArgumentException("Amount must be greater than zero");
		}
		
		return amount;
	}
	
	private static String normalizeOptionalText(String value) {
		return value == null ? "" : value.trim();
	}
	
}
