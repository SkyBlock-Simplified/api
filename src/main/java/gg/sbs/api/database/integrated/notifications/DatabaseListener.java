package gg.sbs.api.database.integrated.notifications;

import java.sql.SQLException;

public interface DatabaseListener {

	void onDatabaseNotification(final DatabaseNotification databaseNotification) throws SQLException;

}