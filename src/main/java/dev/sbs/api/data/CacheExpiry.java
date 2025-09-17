package dev.sbs.api.data;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheExpiry {

    @NotNull CacheExpiry DEFAULT = new CacheExpiry() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return CacheExpiry.class;
        }

        @Override
        public @NotNull TimeUnit length() {
            return TimeUnit.SECONDS;
        }

        @Override
        public int value() {
            return 30;
        }

    };

    @NotNull TimeUnit length();

    int value();

}
