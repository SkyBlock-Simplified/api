package dev.sbs.api;

import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.builder.Builder;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.ArrayUtil;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.tuple.Triple;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("all")
public abstract class SimplifiedException extends RuntimeException {

    @Getter
    private final ConcurrentList<Triple<String, String, Boolean>> fields;

    protected SimplifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.fields = fields;
    }

    public static <T extends SimplifiedException> ExceptionBuilder<T> of(Class<T> eClass) {
        return new ExceptionBuilder<>(eClass);
    }

    public static ExceptionBuilder<WrappedException> wrapNative(Throwable throwable) {
        return of(WrappedException.class).withCause(throwable);
    }

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ExceptionBuilder<T extends SimplifiedException> implements Builder<T> {

        protected final Class<T> eClass;
        protected String message;
        protected boolean enableSuppression = false;
        protected boolean writableStackTrace = true;
        protected Throwable cause;
        private final ConcurrentList<Triple<String, String, Boolean>> fields = Concurrent.newList();

        public ExceptionBuilder<T> addField(String name, String value) {
            return this.addField(name, value, true);
        }

        public ExceptionBuilder<T> addField(String name, String value, boolean inline) {
            this.fields.add(Triple.of(name, value, inline));
            return this;
        }

        public ExceptionBuilder<T> addFields(@NonNull Map.Entry<String, String>... entries) {
            Arrays.stream(entries).forEach(entry -> this.addField(entry.getKey(), entry.getValue()));
            return this;
        }

        public ExceptionBuilder<T> addFields(@NonNull Triple<String, String, Boolean>... fields) {
            Arrays.stream(fields).forEach(field -> this.addField(field.getLeft(), field.getMiddle(), field.getRight()));
            return this;
        }

        public ExceptionBuilder<T> withCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public ExceptionBuilder<T> withMessage(String message, Object... objects) {
            this.message = ArrayUtil.isEmpty(objects) ? message : FormatUtil.format(message, objects);
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
            return Reflection.of(this.eClass).newInstance(this.message, this.cause, this.enableSuppression, this.writableStackTrace, this.fields);
        }

    }

    public static class WrappedException extends SimplifiedException {

        private WrappedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields) {
            super(message, cause, enableSuppression, writableStackTrace, fields);
        }

    }

}
