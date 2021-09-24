package gg.sbs.api.database.integrated.factory.callbacks;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetCallback<T> {

	T handle(ResultSet result) throws SQLException;

}