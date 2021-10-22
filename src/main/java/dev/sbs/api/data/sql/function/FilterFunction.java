package dev.sbs.api.data.sql.function;

import dev.sbs.api.data.sql.model.SqlModel;

import java.util.function.Function;

public interface FilterFunction<T extends SqlModel, R> extends Function<T, R> {

}
