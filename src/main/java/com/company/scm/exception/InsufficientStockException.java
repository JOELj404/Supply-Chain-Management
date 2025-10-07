package com.company.scm.exception;

/**
 * A checked exception thrown when an operation cannot be completed
 * due to a lack of available stock.
 */
public class InsufficientStockException extends Exception {
    private static final long serialVersionUID = 1L;


    public InsufficientStockException(String message) {
        super(message);
    }
}

