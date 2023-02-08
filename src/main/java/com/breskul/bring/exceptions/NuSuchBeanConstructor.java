package com.breskul.bring.exceptions;

/**
 * Throws when there is no such Bean
 *
 * @author Artem Yankovets
 */
public class NuSuchBeanConstructor extends CommonException {

  public static final String CAUSE_BEAN_NAME_PATTERN = "There is no such Bean (name: %s)";
  public static final String SUGGESTED_SOLUTION = "Check the Bean existence";

  public NuSuchBeanConstructor(String location, String beanName) {
    super(location, String.format(CAUSE_BEAN_NAME_PATTERN, beanName), SUGGESTED_SOLUTION);
  }
}
