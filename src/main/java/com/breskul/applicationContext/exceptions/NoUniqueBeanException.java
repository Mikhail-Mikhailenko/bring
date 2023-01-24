package com.breskul.applicationContext.exceptions;

public class NoUniqueBeanException extends RuntimeException {
    public NoUniqueBeanException() {
        super("NO_UNIQUE_BEAN_EXCEPTION");
    }
}
