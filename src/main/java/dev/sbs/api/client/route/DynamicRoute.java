package dev.sbs.api.client.route;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation that creates domain-specific routing annotations that
 * return {@link DynamicRouteProvider} implementations as route providers.
 * <p>
 * Example:
 * <pre><code>
 * {@code @DynamicRoute}
 * public @interface CustomDomain {
 *     CustomClient.Domain value(); // Returns a RouteProvider
 * }
 * </code></pre>
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicRoute {

    /**
     * The method name in the annotated annotation that returns the {@link DynamicRouteProvider}.
     *
     * @return Method name, defaults to "value"
     */
    @NotNull String methodName() default "value";

}
