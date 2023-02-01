package com.breskul.bring.exceptions;

public class CommonException extends RuntimeException {
    public static final String PATTERN = "[%s] %s - %s";
    public CommonException(String location, String cause, String suggestedSolution) {
        super(String.format(PATTERN, location, cause, suggestedSolution));
    }

}
