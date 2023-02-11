package com.breskul.bring.exceptions;

/**
 * Throws when there are some troubles during bean injection process
 */
public class BeanInjectionException extends CommonException {

    public BeanInjectionException(String location, String cause, String solution) {
        super(location, cause, solution);
    }
}
