package com.io.github.msj.delivery.exception;

import org.hibernate.service.spi.ServiceException;

import java.util.Arrays;
import java.util.List;

public class ErrorValidation {

    private List<String> errors;

    public ErrorValidation(List<String> errors) {
        this.errors = errors;
    }

    public ErrorValidation(String message) {
        this.errors = Arrays.asList(message);
    }

    public ErrorValidation(ServiceException exception) {
        this.errors = Arrays.asList(exception.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
