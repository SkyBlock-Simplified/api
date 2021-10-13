package gg.sbs.api.data.sql.integrated.function;

import gg.sbs.api.util.function.ReturnParameterFunction;

import java.sql.ResultSet;

public interface ResultSetFunction<T> extends ReturnParameterFunction<T, ResultSet> {

	T handle(ResultSet result);

}