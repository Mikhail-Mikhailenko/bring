package com.breskul.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicate method produces a bean which will be stored and managed in Bring container.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {

  /**
   * The name of this bean. If left unspecified
   * the name of the bean is the name from annotated method.
   */
  String value() default "";

}
