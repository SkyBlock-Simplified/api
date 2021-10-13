package gg.sbs.api.util.builder;

import gg.sbs.api.util.builder.CoreBuilder;

public interface ClassBuilder<T> extends CoreBuilder {

    <R extends T> R build(Class<R> tClass);

}