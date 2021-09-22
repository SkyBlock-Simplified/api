package gg.sbs.api.database.notifications;

import java.sql.SQLException;

public interface DatabaseListener {

	void onDatabaseNotification(final DatabaseNotification databaseNotification) throws SQLException;

}