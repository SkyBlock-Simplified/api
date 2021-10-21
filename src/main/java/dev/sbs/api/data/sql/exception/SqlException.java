package dev.sbs.api.data.sql.exception;

import java.sql.SQLException;

public class SqlException extends SQLException {

    public SqlException(String message) {
        super(message);
    }

    public SqlException(Throwable throwable) {
        super(throwable);
    }

    public SqlException(String message, Throwable throwable) {
        super(message, throwable);
    }

}