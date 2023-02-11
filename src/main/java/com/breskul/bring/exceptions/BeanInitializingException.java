package com.breskul.bring.exceptions;

/**
 * Throws when there are some troubles during bean initialization process
 *
 * @author Artem Yankovets
 */
public class BeanInitializingException extends CommonException {

  public static final String SUGGESTED_SOLUTION = "";

  public BeanInitializingException(String location, String cause) {
    super(location, cause, SUGGESTED_SOLUTION);
  }

  public BeanInitializingException(String location, String cause, String solution) {
    super(location, cause, solution);
  }

}
