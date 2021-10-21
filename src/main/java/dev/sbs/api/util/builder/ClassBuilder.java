package dev.sbs.api.util.builder;

public interface ClassBuilder<T> extends CoreBuilder {

    <R extends T> R build(Class<R> tClass);

}
