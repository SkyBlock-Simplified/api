package dev.sbs.api.util.builder.annotation;

import dev.sbs.api.util.builder.hash.HashCodeBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to exclude a field from being used by
 * the various {@code reflectionHashcode} methods defined on
 * {@link HashCodeBuilder}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HashCodeExclude {

}
