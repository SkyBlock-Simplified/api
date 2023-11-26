package dev.sbs.api.data.yaml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Flag {

    /**
     * Sets whether to load/save static fields.
     */
    boolean preserveStatic() default false;

    /**
     * Sets whether this field is required.
     */
    boolean required() default false;

    /**
     * Sets whether this field should be censored when publicly rendered.
     */
    boolean secure() default false;

}
