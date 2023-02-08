package com.breskul.bring.exceptions;

/**
 * Throws when no unique Bean found
 *
 * @author Artem Yankovets
 */
public class NoUniqueBeanException extends CommonException {

  public static final String CAUSE_BEAN_NAME_PATTERN = "No qualifying bean of type: %s";
  public static final String SUGGESTED_SOLUTION_PATTERN = "Check the Bean name. It should be unique. Expected single matching bean but found: %d: %s";

  public NoUniqueBeanException(String location, String beanName, int size, String foundBeanNames) {
    super(location,
        String.format(CAUSE_BEAN_NAME_PATTERN, beanName),
        String.format(SUGGESTED_SOLUTION_PATTERN, size, foundBeanNames));
  }
}
