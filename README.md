# Personal Finance Manager

A Java-based console application for managing personal expenses.

The project was built to practice clean Java application architecture, object-oriented programming, persistence, testing, exception handling, collections, lambdas and dependency injection.

## Features

The application allows users to:

- Add expenses
- View all expenses
- Update existing expenses
- Delete expenses
- Filter expenses by:
    - Category
    - Date
    - Merchant
- Sort expenses by:
    - Amount ascending
    - Amount descending
    - Date ascending
    - Date descending
- Calculate the total amount of all expenses
- Calculate the total amount for a specific category
- Calculate totals for all categories
- Persist expenses in a JSON file
- Automatically restore saved expenses when the application starts

## Technologies

- Java 17
- Maven
- JUnit 5
- Jackson
- Java NIO
- Git

## Architecture

The application is divided into several layers:

```text
ConsoleController
       |
       v
ExpenseManager
       |
       v
ExpenseRepository
       |
       +------------------------+
       |                        |
       v                        v
JsonExpenseRepository   InMemoryExpenseRepository
       |
       v
expenses.json