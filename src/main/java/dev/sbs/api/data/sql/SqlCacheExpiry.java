package dev.sbs.api.data.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlCacheExpiry {

    /**
     * How long after creation does this entity expire in seconds.
     *
     * @return expiry time in seconds
     */
    int creation() default 60;

    /**
     * How long after access does this entity expire in seconds.
     *
     * @return expiry time in seconds
     */
    int access() default -1;

    /**
     * How long after updating does this entity in seconds.
     *
     * @return expiry time in seconds
     */
    int update() default 0;

}
