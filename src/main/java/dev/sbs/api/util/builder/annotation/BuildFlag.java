package dev.sbs.api.util.builder.annotation;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
     * Match only one required field in the same group.
     * <br><br>
     * Checks null, {@link Optional#isEmpty()}, {@link Collection#isEmpty()}, {@link Map#isEmpty()}, etc.
     */
    @NotNull String requireGroup() default "";

    /**
     * Should field match a specific RegExp pattern.
     */
    @Language("RegExp")
    @NotNull String pattern() default "";

    /**
     * Limits field maximum length/size.
     * <br><br>
     * Checks {@link CharSequence}, {@link String}, {@link List}, {@link Set}, etc.
     */
    int limit() default -1;

}