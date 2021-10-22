package dev.sbs.api.data.sql.integrated.function;

import java.sql.ResultSet;
import java.util.function.Function;

public interface ResultSetFunction<R> extends Function<ResultSet, R> {

}
