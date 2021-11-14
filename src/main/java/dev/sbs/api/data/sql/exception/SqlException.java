package dev.sbs.api.data.sql.exception;

public class SqlException extends RuntimeException {

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
