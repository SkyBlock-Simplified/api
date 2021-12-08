package dev.sbs.api.data.yaml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigMode {

    Type value() default Type.DEFAULT;

    enum Type {

        DEFAULT,
        FIELD_IS_KEY,
        PATH_BY_UNDERSCORE

    }

}
