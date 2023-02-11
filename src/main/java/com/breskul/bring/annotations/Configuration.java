package com.breskul.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a class declares one or more {@link Bean} methods and
 * using by Bring for generating and getting bean instances.
 * Such classes will be detected and added to the context.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

  /**
   * Specific bean name for Configuration class.
   */
  String value() default "";

}
