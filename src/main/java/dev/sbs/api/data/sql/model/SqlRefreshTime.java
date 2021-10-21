package dev.sbs.api.data.sql.model;

import dev.sbs.api.util.helper.TimeUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlRefreshTime {

    /**
     * How long between refreshing in milliseconds.
     *
     * @return refresh time in milliseconds
     */
    long value() default TimeUtil.ONE_MINUTE_MS;

}
