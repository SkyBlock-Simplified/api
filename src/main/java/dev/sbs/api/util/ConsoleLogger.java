package dev.sbs.api.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachable;
import dev.sbs.api.util.helper.FormatUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.Serializable;
import java.util.Iterator;

public abstract class ConsoleLogger implements AppenderAttachable<ILoggingEvent>, Serializable {
    
    private final Logger log;

    @Getter
    @Setter
    private Level visualLevel;
    
    protected ConsoleLogger(Class<?> tClass) {
        this.log = (Logger) LoggerFactory.getLogger(tClass);
    }

    protected ConsoleLogger(String name) {
        this.log = (Logger) LoggerFactory.getLogger(name);
    }

    @Override
    public final void addAppender(Appender<ILoggingEvent> newAppender) {
        this.log.addAppender(newAppender);
    }

    @Override
    public final Appender<ILoggingEvent> getAppender(String name) {
        return this.log.getAppender(name);
    }

    @Override
    public final void detachAndStopAllAppenders() {
        this.log.detachAndStopAllAppenders();
    }

    @Override
    public final boolean detachAppender(Appender<ILoggingEvent> appender) {
        return this.log.detachAppender(appender);
    }

    @Override
    public final boolean detachAppender(String name) {
        return this.log.detachAppender(name);
    }

    public final Level getEffectiveLevel() {
        return this.log.getEffectiveLevel();
    }

    public final Level getConsoleLevel() {
        return this.log.getLevel();
    }

    public final String getName() {
        return this.log.getName();
    }

    public boolean isAdditive() {
        return this.log.isAdditive();
    }

    @Override
    public final boolean isAttached(Appender<ILoggingEvent> appender) {
        return this.log.isAttached(appender);
    }

    public final boolean isDebugEnabled() {
        return this.isDebugEnabled(null);
    }

    public final boolean isDebugEnabled(Marker marker) {
        return this.log.isDebugEnabled(marker);
    }

    public final boolean isErrorEnabled() {
        return this.isErrorEnabled(null);
    }

    public final boolean isErrorEnabled(Marker marker) {
        return this.log.isErrorEnabled(marker);
    }

    public final boolean isInfoEnabled() {
        return this.isInfoEnabled(null);
    }

    public final boolean isInfoEnabled(Marker marker) {
        return this.log.isTraceEnabled(marker);
    }

    public final boolean isTraceEnabled() {
        return this.isTraceEnabled(null);
    }

    public final boolean isTraceEnabled(Marker marker) {
        return this.log.isTraceEnabled(marker);
    }

    public final boolean isWarnEnabled() {
        return this.isWarnEnabled(null);
    }

    public final boolean isWarnEnabled(Marker marker) {
        return this.log.isWarnEnabled(marker);
    }

    public Iterator<Appender<ILoggingEvent>> iteratorForAppenders() {
        return this.log.iteratorForAppenders();
    }

    public void log(Marker marker, String fqcn, int level, String message, Object[] argArray, Throwable throwable) {
        this.log.log(marker, fqcn, level, message, argArray, throwable);
    }

    public void setAdditive(boolean additive) {
        this.log.setAdditive(additive);
    }

    public void setConsoleLevel(Level newLevel) {
        this.log.setLevel(newLevel);
    }

    @Override
    public String toString() {
        return FormatUtil.format("[{0}]", this.getName());
    }

    // --- Events ---

    protected abstract void onError(String message, Throwable throwable);

    protected abstract void onThrowable(Throwable throwable);

    // --- Logging ---

    public void debug(String message) {
        this._debug(null, message);
    }

    public void debug(String message, Object arg) {
        this._debug(null, FormatUtil.format(message, arg));
    }

    public void debug(String message, Object arg1, Object arg2) {
        this._debug(null, FormatUtil.format(message, arg1, arg2));
    }

    public void debug(String message, Object... arguments) {
        this._debug(null, FormatUtil.format(message, arguments));
    }

    public void debug(String message, Throwable throwable) {
        this._debug(null, message, throwable);
    }

    public void debug(Marker marker, String message) {
        this._debug(marker, message);
    }

    public void debug(Marker marker, String message, Object arg) {
        this._debug(marker, FormatUtil.format(message, arg));
    }

    public void debug(Marker marker, String message, Object arg1, Object arg2) {
        this._debug(marker, FormatUtil.format(message, arg1, arg2));
    }

    public void debug(Marker marker, String message, Object... arguments) {
        this._debug(marker, FormatUtil.format(message, arguments));
    }

    public void debug(Marker marker, String message, Throwable throwable) {
        this._debug(marker, message, throwable);
    }

    private void _debug(Marker marker, String message) {
        this._debug(marker, message, null);
    }

    private void _debug(Marker marker, String message, Throwable throwable) {
        if (throwable != null)
            this.onThrowable(throwable);

        this.log.debug(marker, message, throwable);
    }

    public void error(String message) {
        this._error(null, message);
    }

    public void error(String message, Object arg) {
        this._error(null, FormatUtil.format(message, arg));
    }

    public void error(String message, Object arg1, Object arg2) {
        this._error(null, FormatUtil.format(message, arg1, arg2));
    }

    public void error(String message, Object... arguments) {
        this._error(null, FormatUtil.format(message, arguments));
    }

    public void error(String message, Throwable throwable) {
        this._error(null, message, throwable);
    }

    public void error(Marker marker, String message) {
        this._error(marker, message);
    }

    public void error(Marker marker, String message, Object arg) {
        this._error(marker, FormatUtil.format(message, arg));
    }

    public void error(Marker marker, String message, Object arg1, Object arg2) {
        this._error(marker, FormatUtil.format(message, arg1, arg2));
    }

    public void error(Marker marker, String message, Object... arguments) {
        this._error(marker, FormatUtil.format(message, arguments));
    }

    public void error(Marker marker, String message, Throwable throwable) {
        this._error(marker, message, throwable);
    }

    private void _error(Marker marker, String message) {
        this._error(marker, message, null);
    }

    private void _error(Marker marker, String message, Throwable throwable) {
        this.onError(message, throwable);

        if (throwable != null)
            this.onThrowable(throwable);

        this.log.error(marker, message, throwable);
    }

    public void info(String message) {
        this._info(null, message);
    }

    public void info(String message, Object arg) {
        this._info(null, FormatUtil.format(message, arg));
    }

    public void info(String message, Object arg1, Object arg2) {
        this._info(null, FormatUtil.format(message, arg1, arg2));
    }

    public void info(String message, Object... arguments) {
        this._info(null, FormatUtil.format(message, arguments));
    }

    public void info(String message, Throwable throwable) {
        this._info(null, message, throwable);
    }

    public void info(Marker marker, String message) {
        this._info(marker, message);
    }

    public void info(Marker marker, String message, Object arg) {
        this._info(marker, FormatUtil.format(message, arg));
    }

    public void info(Marker marker, String message, Object arg1, Object arg2) {
        this._info(marker, FormatUtil.format(message, arg1, arg2));
    }

    public void info(Marker marker, String message, Object... arguments) {
        this._info(marker, FormatUtil.format(message, arguments));
    }

    public void info(Marker marker, String message, Throwable throwable) {
        this._info(marker, message, throwable);
    }

    private void _info(Marker marker, String message) {
        this._info(marker, message, null);
    }

    private void _info(Marker marker, String message, Throwable throwable) {
        if (throwable != null)
            this.onThrowable(throwable);

        this.log.info(marker, message, throwable);
    }

    public void trace(String message) {
        this._trace(null, message);
    }

    public void trace(String message, Object arg) {
        this._trace(null, FormatUtil.format(message, arg));
    }

    public void trace(String message, Object arg1, Object arg2) {
        this._trace(null, FormatUtil.format(message, arg1, arg2));
    }

    public void trace(String message, Object... arguments) {
        this._trace(null, FormatUtil.format(message, arguments));
    }

    public void trace(String message, Throwable throwable) {
        this._trace(null, message, throwable);
    }

    public void trace(Marker marker, String message) {
        this._trace(marker, message);
    }

    public void trace(Marker marker, String message, Object arg) {
        this._trace(marker, FormatUtil.format(message, arg));
    }

    public void trace(Marker marker, String message, Object arg1, Object arg2) {
        this._trace(marker, FormatUtil.format(message, arg1, arg2));
    }

    public void trace(Marker marker, String message, Object... arguments) {
        this._trace(marker, FormatUtil.format(message, arguments));
    }

    public void trace(Marker marker, String message, Throwable throwable) {
        this._trace(marker, message, throwable);
    }

    private void _trace(Marker marker, String message) {
        this._trace(marker, message, null);
    }

    private void _trace(Marker marker, String message, Throwable throwable) {
        if (throwable != null)
            this.onThrowable(throwable);

        this.log.trace(marker, message, throwable);
    }

    public void warn(String message) {
        this._warn(null, message);
    }

    public void warn(String message, Object arg) {
        this._warn(null, FormatUtil.format(message, arg));
    }

    public void warn(String message, Object arg1, Object arg2) {
        this._warn(null, FormatUtil.format(message, arg1, arg2));
    }

    public void warn(String message, Object... arguments) {
        this._warn(null, FormatUtil.format(message, arguments));
    }

    public void warn(String message, Throwable throwable) {
        this._warn(null, message, throwable);
    }

    public void warn(Marker marker, String message) {
        this._warn(marker, message);
    }

    public void warn(Marker marker, String message, Object arg) {
        this._warn(marker, FormatUtil.format(message, arg));
    }

    public void warn(Marker marker, String message, Object arg1, Object arg2) {
        this._warn(marker, FormatUtil.format(message, arg1, arg2));
    }

    public void warn(Marker marker, String message, Object... arguments) {
        this._warn(marker, FormatUtil.format(message, arguments));
    }

    public void warn(Marker marker, String message, Throwable throwable) {
        this._warn(marker, message, throwable);
    }

    private void _warn(Marker marker, String message) {
        this._warn(marker, message, null);
    }

    private void _warn(Marker marker, String message, Throwable throwable) {
        if (throwable != null)
            this.onThrowable(throwable);

        this.log.warn(marker, message, throwable);
    }

}
