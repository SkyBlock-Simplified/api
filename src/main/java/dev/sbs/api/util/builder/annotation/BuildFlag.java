package dev.sbs.api.util.builder.annotation;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BuildFlag {

    /**
     * Should field be null/empty checked.
     * <br><br>
     * Checks null, {@link Optional#isEmpty()}, {@link Collection#isEmpty()}, {@link Map#isEmpty()}, etc.
     */
    boolean required() default false;

    /**
     * Should field match a specific RegExp pattern.
     */
    @Language("RegExp")
    @NotNull String pattern() default "";

}