package com.company.scm.exception;

/**
 * A checked exception thrown when a lookup for a specific entity
 * by its identifier (e.g., ID) yields no result.
 */
public class EntityNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;


    public EntityNotFoundException(String message) {
        super(message);
    }
}