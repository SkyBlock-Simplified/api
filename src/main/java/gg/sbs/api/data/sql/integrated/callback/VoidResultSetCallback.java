package gg.sbs.api.data.sql.integrated.callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface VoidResultSetCallback {

	void handle(ResultSet result) throws SQLException;

}