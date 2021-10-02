package gg.sbs.api.data.sql.integrated.callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetCallback<T> {

	T handle(ResultSet result) throws SQLException;

}