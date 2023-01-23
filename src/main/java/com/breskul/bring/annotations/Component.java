package com.breskul.bring.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class is a component of framework.
 * Such classes will be detected and added to the context if it will be found
 * in scope of automatic scan packages.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

  /**
   * The name of this component.
   */
  String value() default "";

}
