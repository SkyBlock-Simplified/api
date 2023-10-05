package dev.sbs.api.util;

import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.builder.Builder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.Triple;
import dev.sbs.api.util.helper.ArrayUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("all")
public abstract class SimplifiedException extends RuntimeException {

    @Getter private final ConcurrentList<Triple<String, String, Boolean>> fields;
    @Getter private final ConcurrentMap<String, Object> data;

    protected SimplifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.fields = Concurrent.newUnmodifiableList(fields);
        this.data = Concurrent.newUnmodifiableMap(data);
    }

    public static <T extends SimplifiedException> ExceptionBuilder<T> of(Class<T> eClass) {
        return new ExceptionBuilder<>(eClass);
    }

    /**
     * Wraps a native Java exception in {@link WrappedException} to escape nested try-catch clauses.
     *
     * @param throwable The thrown exception.
     * @return A wrapped runtime exception.
     */
    public static ExceptionBuilder<WrappedException> wrapNative(Throwable throwable) {
        return of(WrappedException.class).withCause(throwable).withMessage(throwable.getMessage());
    }

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ExceptionBuilder<T extends SimplifiedException> implements Builder<T> {

        protected final Class<T> eClass;
        protected String message;
        protected boolean enableSuppression = false;
        protected boolean writableStackTrace = true;
        protected Throwable cause;
        private final ConcurrentList<Triple<String, String, Boolean>> fields = Concurrent.newList();
        private final ConcurrentMap<String, Object> data = Concurrent.newMap();

        public ExceptionBuilder<T> addData(String key, Object value) {
            this.data.put(key, value);
            return this;
        }

        public ExceptionBuilder<T> withField(String name, String value) {
            return this.withField(name, value, false);
        }

        public ExceptionBuilder<T> withField(String name, String value, boolean inline) {
            this.fields.add(Triple.of(name, value, inline));
            return this;
        }

        public ExceptionBuilder<T> withFields(@NotNull Map.Entry<String, String>... entries) {
            Arrays.stream(entries).forEach(entry -> this.withField(entry.getKey(), entry.getValue()));
            return this;
        }

        public ExceptionBuilder<T> withFields(@NotNull Triple<String, String, Boolean>... fields) {
            Arrays.stream(fields).forEach(field -> this.withField(field.getLeft(), field.getMiddle(), field.getRight()));
            return this;
        }

        public ExceptionBuilder<T> withCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public ExceptionBuilder<T> withMessage(String message, Object... objects) {
            this.message = ArrayUtil.isEmpty(objects) ? message : String.format(message, objects);
            return this;
        }

        public ExceptionBuilder<T> withSuppression() {
            return this.withSuppression(true);
        }

        public ExceptionBuilder<T> withSuppression(boolean value) {
            this.enableSuppression = value;
            return this;
        }

        public ExceptionBuilder<T> withStackTrace() {
            return this.withStackTrace(true);
        }

        public ExceptionBuilder<T> withStackTrace(boolean value) {
            this.writableStackTrace = value;
            return this;
        }

        @Override
        public T build() {
            return Reflection.of(this.eClass).newInstance(this.message, this.cause, this.enableSuppression, this.writableStackTrace, this.fields, this.data);
        }

    }

    public static class WrappedException extends SimplifiedException {

        private WrappedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
            super(message, cause, enableSuppression, writableStackTrace, fields, data);
        }

    }

}
