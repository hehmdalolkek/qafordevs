package ru.hehmdalolkek.qafordevs.exception;

public class DeveloperWithDuplicateEmailException extends RuntimeException {
    public DeveloperWithDuplicateEmailException(String message) {
        super(message);
    }
}
