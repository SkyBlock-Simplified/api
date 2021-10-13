package gg.sbs.api.manager.builder.implementation;

public interface ClassBuilder<T> extends CoreBuilder {

    <R extends T> R build(Class<R> tClass);

}