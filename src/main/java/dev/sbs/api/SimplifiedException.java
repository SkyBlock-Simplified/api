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

    public static <T extends Throwable> NativeExceptionBuilder<T> ofNative(Class<T> eClass) {
        return new NativeExceptionBuilder<>(eClass);
    }

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NativeExceptionBuilder<T extends Throwable> implements Builder<T> {

        protected final Class<T> eClass;
        protected String message;
        protected boolean enableSuppression = false;
        protected boolean writableStackTrace = true;
        protected Throwable cause;

        public NativeExceptionBuilder<T> withMessage(String message, Object... objects) {
            this.message = ArrayUtil.isEmpty(objects) ? message : FormatUtil.format(message, objects);
            return this;
        }

        public NativeExceptionBuilder<T> withSuppression(boolean value) {
            this.enableSuppression = value;
            return this;
        }

        public NativeExceptionBuilder<T> withStackTrace(boolean value) {
            this.writableStackTrace = value;
            return this;
        }

        public NativeExceptionBuilder<T> withCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T build() {
            return (T) new Reflection(this.eClass).newInstance(this.message, this.cause, this.enableSuppression, this.writableStackTrace);
        }

    }

    public static class ExceptionBuilder<T extends SimplifiedException> extends NativeExceptionBuilder<T> {

        private final ConcurrentList<Triple<String, String, Boolean>> fields = Concurrent.newList();

        private ExceptionBuilder(Class<T> eClass) {
            super(eClass);
        }

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

        @Override
        public ExceptionBuilder<T> withMessage(@NonNull String message, Object... objects) {
            super.withMessage(message, objects);
            return this;
        }

        @Override
        public ExceptionBuilder<T> withSuppression(boolean value) {
            super.withSuppression(value);
            return this;
        }

        @Override
        public ExceptionBuilder<T> withStackTrace(boolean value) {
            super.withStackTrace(value);
            return this;
        }

        @Override
        public ExceptionBuilder<T> withCause(Throwable cause) {
            super.withCause(cause);
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T build() {
            return (T) new Reflection(this.eClass).newInstance(this.message, this.cause, this.enableSuppression, this.writableStackTrace, this.fields);
        }

    }

}
