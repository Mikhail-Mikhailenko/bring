package com.breskul.bring.exceptions;

/**
 * Throws when Beans not found
 *
 * @author Artem Yankovets
 */
public class NoSuchBeanException extends CommonException {

  public static final String CAUSE_BEAN_TYPE_PATTERN = "The Bean (type: %s) not found";
  public static final String CAUSE_BEAN_NAME_AND_TYPE_PATTERN = "The Bean (name: %s, type: %s) not found";
  public static final String SUGGESTED_SOLUTION = "Check that Bean was initialized";

  public NoSuchBeanException(String location, String beanType) {
    super(location,
        String.format(CAUSE_BEAN_TYPE_PATTERN, beanType),
        SUGGESTED_SOLUTION
    );
  }

  public NoSuchBeanException(String location, String beanName, String beanType) {
    super(location,
        String.format(CAUSE_BEAN_NAME_AND_TYPE_PATTERN, beanName, beanType),
        SUGGESTED_SOLUTION
    );
  }


}
