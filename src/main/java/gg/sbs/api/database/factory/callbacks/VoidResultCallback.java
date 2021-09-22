package gg.sbs.api.database.factory.callbacks;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface VoidResultCallback {

	void handle(ResultSet result) throws SQLException;

}