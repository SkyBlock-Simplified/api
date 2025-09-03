package dev.sbs.api.data.json;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonResource {

    /**
     * Retrieves the path for the JSON resource.
     * <ul>
     * <li>Defaults to an empty string if unspecified.</li>
     * </ul>
     *
     * @return A {@code String} representing the resource path.
     */
    @NotNull String path() default "";

    /**
     * Retrieves the name of the resource.
     *
     * @return A {@code String} representing the name of the json resource.
     */
    @NotNull String name();

    /**
     * Defines mandatory {@link JsonModel} dependencies for the annotated resource.
     *
     * @return An array of {@code Class} objects extending {@code JsonModel} that are required for the resource.
     */
    @NotNull Class<? extends JsonModel>[] required() default {};

}
