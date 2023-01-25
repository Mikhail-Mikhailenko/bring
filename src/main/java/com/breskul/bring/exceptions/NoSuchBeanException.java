package com.breskul.bring.exceptions;

/**
 * Beans not found exception
 */
public class NoSuchBeanException extends RuntimeException{
    public NoSuchBeanException(String message) {
        super(message);
    }
}
