package dev.sbs.api.reflection.accessor;

import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.reflection.exception.ReflectionException;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.util.Optional;

@RequiredArgsConstructor
abstract class ReflectionAccessor<T extends AccessibleObject> {

    /**
     * Gets the reflection object associated with this accessor.
     */
    @Getter
    private final Reflection<?> reflection;

    /**
     * Gets the instance of type {@link T}.
     */
    @Getter
    private final T handle;

    @Override
    public final boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ReflectionAccessor<?> other)) return false;
        return new EqualsBuilder().append(this.getType(), other.getType()).append(this.getHandle(), other.getHandle()).build();
    }

    public final <A extends Annotation> Optional<A> getAnnotation(Class<A> annotationClass) {
        return Optional.ofNullable(this.getHandle().isAnnotationPresent(annotationClass) ? this.getHandle().getAnnotation(annotationClass) : null);
    }

    /**
     * Gets the class object associated with this accessor.
     * <p>
     * This object is cached after the first call.
     *
     * @return The class object.
     * @throws ReflectionException When the class cannot be located.
     */
    public final Class<?> getType() throws ReflectionException {
        return this.getReflection().getType();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(this.getType()).append(this.getHandle()).build();
    }

}
