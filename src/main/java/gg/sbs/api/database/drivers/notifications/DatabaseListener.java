package gg.sbs.api.database.drivers.notifications;

import java.sql.SQLException;

public interface DatabaseListener {

	void onDatabaseNotification(final DatabaseNotification databaseNotification) throws SQLException;

}