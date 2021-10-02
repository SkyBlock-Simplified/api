package gg.sbs.api.data.yaml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigMode {

	Type value() default Type.DEFAULT;

	enum Type {
		DEFAULT,
		FIELD_IS_KEY,
		PATH_BY_UNDERSCORE
	}

}