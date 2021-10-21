package dev.sbs.api.data.sql.integrated.notification;

import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.StringUtil;
import dev.sbs.api.data.sql.integrated.function.ResultSetFunction;
import dev.sbs.api.data.sql.integrated.function.VoidResultSetFunction;
import dev.sbs.api.data.sql.integrated.factory.SQLFactory;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 * An sql listener used to check for updates to its associated table and notify plugins.
 */
@SuppressWarnings("all")
public class DatabaseNotification {

	private final ConcurrentList<String> primaryColumnNames = Concurrent.newList();
	private final transient DatabaseListener listener;
	private final transient SQLFactory sql;
	private volatile boolean stopped = false;
	private TriggerEvent event;
	private int previousId = 0;
	private final String table;

	DatabaseNotification(SQLFactory sql, String table, DatabaseListener listener, boolean overwrite) throws SQLException {
		if (listener == null)
			throw new IllegalArgumentException("DatabaseListener cannot be null!");

		this.sql = sql;
		this.table = table;
		this.pulse();
		this.listener = listener;
		this.loadPrimaryKeys();

		if (!this.triggersExist() || overwrite) {
			for (TriggerEvent event : TriggerEvent.values()) {
				this.dropTrigger(event);
				this.createTrigger(event);
			}
		}
	}

	private void createTrigger(TriggerEvent event) throws SQLException {
		if (!this.primaryColumnNames.isEmpty()) {
			String primaryKeys = StringUtil.join(",", this.primaryColumnNames);
			String quote = this.sql.getIdentifierQuoteString();
			String trigger = FormatUtil.format("CREATE TRIGGER {1}.{2} AFTER {3} ON {0}{4}{0} FOR EACH ROW INSERT INTO {1}.{5} (schema_name, table_name, sql_action, primary_keys, _submitted, old_data, new_data) VALUES (?, ?, ?, ?, UNIX_TIMESTAMP(), ",
					quote, this.getSchema(), this.getName(event), event.toUppercase(), this.getTable(), SQLNotifications.ACTIVITY_TABLE);
			String _old = null;
			String _new = null;

			if (TriggerEvent.INSERT != event)
				_old = FormatUtil.format("CONCAT(OLD.{0}{1}{0})", quote, StringUtil.join(FormatUtil.format("{0}, '','', OLD.{0}", quote), this.primaryColumnNames));

			if (TriggerEvent.DELETE != event)
				_new = FormatUtil.format("CONCAT(NEW.{0}{1}{0})", quote, StringUtil.join(FormatUtil.format("{0}, '','', NEW.{0}", quote), this.primaryColumnNames));

			try {
				this.sql.update(String.format(trigger + "%s, %s);", _old, _new), this.getSchema(), this.getTable(), event.toUppercase(), primaryKeys);
			} catch (SQLException sqlException) {
				if (sqlException.getMessage().contains("Access denied")) {
					String privilege = (sqlException.getMessage().contains("SUPER") ? "SUPER" : sqlException.getMessage().contains("TRIGGER") ? "TRIGGER" : "");

					if (StringUtil.isNotEmpty(privilege))
						throw new SQLException(FormatUtil.format("Cannot create trigger ''{0}''.''{1}''! SQL user lacks {2} privilege! Notifications will not work!", this.getSchema(), this.getName(event), privilege));
				}

				throw sqlException;
			}
		} else
			throw new SQLException(FormatUtil.format("The table {0}.{1} has no primary key columns to keep track of!", this.getSchema(), this.getTable()));
	}

	private void dropTrigger(TriggerEvent event) {
		try {
			this.sql.update(FormatUtil.format("DROP TRIGGER IF EXISTS {0};", this.getName(event)));
		} catch (Exception ignore) { }
	}

	/**
	 * Gets the primary keys and associated deleted data of the current notification.
	 *
	 * @return Map of primary keys and associated deleted data.
	 * @throws SQLException If you attempt to retrieve deleted data when inserting a record.
	 */
	public final Map<String, Object> getDeletedData() throws SQLException {
		if (this.getEvent() == TriggerEvent.INSERT)
			throw new SQLException("Cannot retrieve an inserted record!");

		final ConcurrentMap<String, Object> deleted = Concurrent.newMap();

		this.sql.query(FormatUtil.format("SELECT old_data FROM {0} WHERE schema_name = ? AND table_name = ? AND sql_action = ? AND id = ?;", SQLNotifications.ACTIVITY_TABLE), new VoidResultSetFunction() {
			@Override
			@SneakyThrows
			public void handle(ResultSet result) {
				if (result.next()) {
					String[] _old = result.getString("old_data").split(",");
					int keyCount = primaryColumnNames.size();

					for (int i = 0; i < keyCount; i++)
						deleted.put(primaryColumnNames.get(i), _old[i]);
				}
			}
		}, this.getSchema(), this.getTable(), this.getEvent().toUppercase(), this.previousId);

		return deleted;
	}

	/**
	 * Gets the event of the current notification.
	 *
	 * @return Event type of the current notification.
	 */
	public final TriggerEvent getEvent() {
		return this.event;
	}

	private String getName(TriggerEvent event) {
		return FormatUtil.format("on{0}{1}", this.getTable(), event.toUppercase());
	}

	/**
	 * Gets the schema of the current notification.
	 *
	 * @return Database name of the current notification.
	 */
	public final String getSchema() {
		return this.sql.getSchema();
	}

	/**
	 * Gets the table of the current notification.
	 *
	 * @return Table name of the current notification.
	 */
	public final String getTable() {
		return this.table;
	}

	/**
	 * Gets the updated data of the current notification.
	 *
	 * @param callback Callback class to handle retrieved data.
	 * @throws SQLException If you attempt to retrieve updated data when deleting a record.
	 */
	public final void getUpdatedRow(final VoidResultSetFunction callback) throws SQLException {
		if (this.getEvent() == TriggerEvent.DELETE)
			throw new SQLException("Cannot retrieve a deleted record!");

		this.sql.query(FormatUtil.format("SELECT new_data FROM {0} WHERE schema_name = ? AND table_name = ? AND sql_action = ? AND id = ?;", SQLNotifications.ACTIVITY_TABLE), new VoidResultSetFunction() {
			@Override
			@SneakyThrows
			public void handle(ResultSet result) {
				if (result.next()) {
					ConcurrentList<String> whereClause = Concurrent.newList();
					int keyCount = primaryColumnNames.size();
					String[] _new = result.getString("new_data").split(",");

					if (keyCount > 0) {
						for (int i = 0; i < keyCount; i++)
							whereClause.add(FormatUtil.format("SUBSTRING_INDEX(SUBSTRING_INDEX({0}{1}{0}, '','', {2}), '','', -1) = ?", sql.getIdentifierQuoteString(), primaryColumnNames.get(i), (i + 1)));

						sql.query(FormatUtil.format("SELECT * FROM {0} WHERE {1};", getTable(), StringUtil.join(" AND ", whereClause)), callback, (Object[])_new);
					}
				}
			}
		}, this.getSchema(), this.getTable(), this.getEvent().toUppercase(), this.previousId);
	}

	/**
	 * Gets if the current notification has stopped.
	 *
	 * @return True if has stopped, otherwise false.
	 */
	public final boolean isStopped() {
		return this.stopped;
	}

	private void loadPrimaryKeys() throws SQLException {
		this.primaryColumnNames.clear();
		this.primaryColumnNames.addAll(this.sql.query("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND COLUMN_KEY = ?;", new ResultSetFunction<Collection<? extends String>>() {
			@Override
			@SneakyThrows
			public Collection<? extends String> handle(ResultSet result) {
				ConcurrentList<String> keyNames = Concurrent.newList();
				while (result.next()) keyNames.add(result.getString("COLUMN_NAME"));
				return keyNames;
			}
		}, this.getSchema(), this.getTable(), "PRI"));
	}

	boolean pulse() {
		if (this.isStopped()) return false;

		try {
			return this.sql.query(FormatUtil.format("SELECT id, sql_action FROM {0} WHERE table_name = ? AND id > ? AND sql_action IN (?, ?, ?) ORDER BY id {1}SC LIMIT 1;", SQLNotifications.ACTIVITY_TABLE, (this.previousId == 0 ? "DE" : "A")), new ResultSetFunction<Boolean>() {
				@Override
				@SneakyThrows
				public Boolean handle(ResultSet result) {
					if (result.next()) {
						int last = result.getInt("id");

						if (last > previousId) {
							previousId = last;
							event = TriggerEvent.fromString(result.getString("sql_action"));
							return true;
						}
					}

					return false;
				}
			}, this.getTable(), this.previousId, "INSERT", "UPDATE", "DELETE");
		} catch (SQLException sqlException) {
			// TODO: LOG
			//NiftyCore.getNiftyLogger().log(Level.SEVERE, FormatUtil.format("Unable to query activity table ''{0}''!", SQLNotifications.ACTIVITY_TABLE), ex);
			this.stop();
		}

		return false;
	}

	void sendNotification() {
		try {
			this.listener.onDatabaseNotification(this);
		} catch (SQLException ex) {
			// TODO: Log
			//NiftyCore.getNiftyLogger().log(Level.SEVERE, "Database notification was mishandled!", ex);
		}
	}

	/**
	 * Stops this class from listening for further notifications.
	 */
	public final void stop() {
		this.stop(false);
	}

	/**
	 * Stops this class from listening for further notifications, and optionally delete the trigger from the database.
	 *
	 * @param dropTriggers True to delete the triggers, otherwise false.
	 */
	public final void stop(boolean dropTriggers) {
		this.stopped = true;

		if (dropTriggers) {
			for (TriggerEvent event : TriggerEvent.values())
				this.dropTrigger(event);
		}
	}

	private boolean triggersExist() {
		try {
			return this.sql.query("SELECT TRIGGER_NAME, ACTION_STATEMENT FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_SCHEMA = ? AND TRIGGER_NAME IN (?, ?, ?);", new ResultSetFunction<Boolean>() {
				@Override
				@SneakyThrows
				public Boolean handle(ResultSet result) {
					int valid = 0;

					while (result.next()) {
						String action = result.getString("ACTION_STATEMENT");

						if (action.startsWith(FormatUtil.format("INSERT INTO {0}.{1}", DatabaseNotification.this.getSchema(), SQLNotifications.ACTIVITY_TABLE))) {
							if (action.contains(FormatUtil.format("VALUES (''{0}'', ''{1}''", DatabaseNotification.this.getSchema(), DatabaseNotification.this.getTable())))
								valid++;
						}
					}

					return valid == 3;
				}
			}, this.getSchema(), this.getName(TriggerEvent.INSERT), this.getName(TriggerEvent.UPDATE), this.getName(TriggerEvent.DELETE));
		} catch (Exception ex) {
			// TODO: Log
			//NiftyCore.getNiftyLogger().log(Level.SEVERE, "Unable to check if trigger exists!", ex);
		}

		return false;
	}

}
