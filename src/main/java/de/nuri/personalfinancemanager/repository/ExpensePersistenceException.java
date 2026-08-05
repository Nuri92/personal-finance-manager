package de.nuri.personalfinancemanager.repository;

public class ExpensePersistenceException extends RuntimeException {
	public ExpensePersistenceException(String message) {
		super(message);
	}
	
	public ExpensePersistenceException(String message, Throwable cause) {
		super(message, cause);
	}
}
