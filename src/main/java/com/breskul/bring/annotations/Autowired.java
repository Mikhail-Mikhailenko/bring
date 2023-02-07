package com.breskul.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a constructor or field as to be
 * auto-injected in scope of context initializing.
 *
 * <p>In case of a {@link java.util.Collection} or {@link java.util.Map}
 * dependency type, the Bring will autowire all beans matching the
 * declared value type.
 *
 */

@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
