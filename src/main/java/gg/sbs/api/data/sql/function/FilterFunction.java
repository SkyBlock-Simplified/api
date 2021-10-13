package gg.sbs.api.data.sql.function;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.util.function.ReturnParameterFunction;

public interface FilterFunction<P extends SqlModel, R> extends ReturnParameterFunction<R, P> {

    R handle(P parameter);

}