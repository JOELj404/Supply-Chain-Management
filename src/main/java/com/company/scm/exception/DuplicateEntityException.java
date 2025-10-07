package com.company.scm.exception;

public class DuplicateEntityException extends Exception {

    /**
     * A unique version identifier for this serializable class.
     */
    private static final long serialVersionUID = 1L;

    public DuplicateEntityException(String message) {
        super(message);
    }
}