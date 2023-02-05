package com.breskul.bring.exceptions;

/**
 * Throws when no unique Bean found
 *
 * @author Artem Yankovets
 */
public class NoUniqueBeanException extends CommonException {

  public static final String CAUSE_BEAN_NAME_PATTERN = "The bean (name: %s) is not unique";
  public static final String SUGGESTED_SOLUTION = "Check the Bean name. It should be unique";

  public NoUniqueBeanException(String location, String beanName) {
    super(location, String.format(CAUSE_BEAN_NAME_PATTERN, beanName), SUGGESTED_SOLUTION);
  }
}
