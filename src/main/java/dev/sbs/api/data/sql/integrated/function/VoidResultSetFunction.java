package dev.sbs.api.data.sql.integrated.function;

import dev.sbs.api.util.function.VoidParameterFunction;

import java.sql.ResultSet;

public interface VoidResultSetFunction extends VoidParameterFunction<ResultSet> {

	void handle(ResultSet result);

}