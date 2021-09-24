package gg.sbs.api.data.sql.integrated.factory.callbacks;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetCallback<T> {

	T handle(ResultSet result) throws SQLException;

}