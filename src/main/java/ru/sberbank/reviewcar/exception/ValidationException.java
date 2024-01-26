package ru.sberbank.reviewcar.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String str) {
        super(str);
    }
}