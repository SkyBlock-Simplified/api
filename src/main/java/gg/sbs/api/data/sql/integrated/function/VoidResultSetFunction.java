package gg.sbs.api.data.sql.integrated.function;

import gg.sbs.api.util.function.VoidParameterFunction;

import java.sql.ResultSet;

public interface VoidResultSetFunction extends VoidParameterFunction<ResultSet> {

	void handle(ResultSet result);

}