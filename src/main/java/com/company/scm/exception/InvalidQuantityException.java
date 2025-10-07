package com.company.scm.exception;

/**
 * A checked exception thrown when a method receives a quantity
 * that is zero or negative.
 */
public class InvalidQuantityException extends Exception {
	
    private static final long serialVersionUID = 1L;


    public InvalidQuantityException(String message) {
        super(message);
    }
}