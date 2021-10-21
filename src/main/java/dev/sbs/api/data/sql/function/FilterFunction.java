package dev.sbs.api.data.sql.function;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.util.function.ReturnParameterFunction;

public interface FilterFunction<P extends SqlModel, R> extends ReturnParameterFunction<R, P> {

    R handle(P parameter);

}
