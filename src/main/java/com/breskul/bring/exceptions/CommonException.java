package com.breskul.bring.exceptions;

/**
 * Common exception class<br> Extends the {@link RuntimeException}<br> Each exception class in the
 * project should extend this class
 *
 * @author Artem Yankovets
 */
public abstract class CommonException extends RuntimeException {

  public static final String PATTERN = "[%s] %s - %s";

  public CommonException(String location, String cause, String suggestedSolution) {
    super(String.format(PATTERN, location, cause, suggestedSolution));
  }

}
