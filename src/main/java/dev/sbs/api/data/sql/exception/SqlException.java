package dev.sbs.api.data.sql.exception;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.tuple.Triple;

public class SqlException extends DataException {

    protected SqlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields) {
        super(message, cause, enableSuppression, writableStackTrace, fields);
    }

}
