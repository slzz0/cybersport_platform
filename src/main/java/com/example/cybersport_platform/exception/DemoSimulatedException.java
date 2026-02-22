package com.example.cybersport_platform.exception;

/**
 * Исключение для демонстрации поведения транзакций (частичное сохранение / откат).
 * Выбрасывается намеренно при simulateError=true в GameWithTeamsService.
 */
public class DemoSimulatedException extends RuntimeException {
    public DemoSimulatedException(String message) {
        super(message);
    }
}
