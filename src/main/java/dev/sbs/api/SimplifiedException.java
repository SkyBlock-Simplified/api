package dev.sbs.api;

import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.builder.Builder;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.ArrayUtil;
import dev.sbs.api.util.helper.FormatUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

public abstract class SimplifiedException extends RuntimeException {

    @Getter
    private final ConcurrentMap<String, Object> fields;

    protected SimplifiedException() {
        throw new UnsupportedOperationException("Do not instantiate in this manner!");
    }

    private SimplifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentMap<String, Object> fields) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.fields = Concurrent.newUnmodifiableMap(fields);
    }

    public static <T extends SimplifiedException> SimplifiedBuilder<T> builder(Class<T> eClass) {
        return new SimplifiedBuilder<>(eClass);
    }

    public static <T extends Throwable> NativeBuilder<T> nativeBuilder(Class<T> eClass) {
        return new NativeBuilder<>(eClass);
    }

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NativeBuilder<T extends Throwable> implements Builder<T> {

        protected final Class<T> eClass;
        protected String message;
        protected boolean enableSuppression = false;
        protected boolean writableStackTrace = true;
        protected Throwable cause;

        public NativeBuilder<T> setMessage(String message, Object... objects) {
            this.message = ArrayUtil.isEmpty(objects) ? message : FormatUtil.format(message, objects);
            return this;
        }

        public NativeBuilder<T> setEnableSuppression(boolean value) {
            this.enableSuppression = value;
            return this;
        }

        public NativeBuilder<T> setWritableStackTrace(boolean value) {
            this.writableStackTrace = value;
            return this;
        }

        public NativeBuilder<T> setCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T build() {
            return (T) new Reflection(this.eClass).newInstance(this.message, this.cause, this.enableSuppression, this.writableStackTrace);
        }

    }

    public static class SimplifiedBuilder<T extends SimplifiedException> extends NativeBuilder<T> implements Builder<T> {

        private final ConcurrentMap<String, Object> fields = Concurrent.newMap();

        private SimplifiedBuilder(Class<T> eClass) {
            super(eClass);
        }

        public SimplifiedBuilder<T> addField(Map.Entry<String, Object> entry) {
            return this.addField(entry.getKey(), entry.getValue());
        }

        public SimplifiedBuilder<T> addField(String name, Object value) {
            if (!this.fields.containsKey(name))
                this.fields.put(name, value);

            return this;
        }

        @Override
        public SimplifiedBuilder<T> setMessage(String message, Object... objects) {
            super.setMessage(message, objects);
            return this;
        }

        @Override
        public SimplifiedBuilder<T> setEnableSuppression(boolean value) {
            super.setEnableSuppression(value);
            return this;
        }

        @Override
        public SimplifiedBuilder<T> setWritableStackTrace(boolean value) {
            super.setWritableStackTrace(value);
            return this;
        }

        @Override
        public SimplifiedBuilder<T> setCause(Throwable cause) {
            super.setCause(cause);
            return this;
        }

        public SimplifiedBuilder<T> setField(Map.Entry<String, Object> entry) {
            return this.setField(entry.getKey(), entry.getValue());
        }

        public SimplifiedBuilder<T> setField(String name, Object value) {
            this.fields.put(name, value);
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T build() {
            return (T) new Reflection(this.eClass).newInstance(this.message, this.cause, this.enableSuppression, this.writableStackTrace, this.fields);
        }

    }

}
