package com.breskul.bring.exceptions;

/**
 * Throws when there are some troubles during bean initialization process
 *
 * @author Artem Yankovets
 */
public class BeanInitializingException extends CommonException {

  public static final String CAUSE = "";
  public static final String SUGGESTED_SOLUTION = "";

  public BeanInitializingException(String location) {
    super(location, CAUSE, SUGGESTED_SOLUTION);
  }

}
