package dev.sbs.api.data.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.tuple.Triple;

public class DataException extends SimplifiedException {

    protected DataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields) {
        super(message, cause, enableSuppression, writableStackTrace, fields);
    }

}
