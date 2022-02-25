package dev.sbs.api.util.date;

import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public abstract class CustomDate {

    private static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Get RealTime in milliseconds.
     */
    @Getter private final long realTime;

    /**
     * The TimeZone used in {@link #toString()}. Defaults to EST.
     */
    @Getter private transient TimeZone timeZone;

    /**
     * The DateTimeFormatter used in {@link #toString()}. Defaults to {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
     */
    @Getter @Setter
    private transient DateTimeFormatter dateFormat;

    protected CustomDate(long realTime) {
        this.realTime = realTime;
        this.dateFormat = DEFAULT_DATE_FORMAT;
        this.timeZone = TimeZone.getTimeZone("EST");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomDate that = (CustomDate) o;

        return new EqualsBuilder()
            .append(this.getRealTime(), that.getRealTime())
            .append(this.getTimeZone(), that.getTimeZone())
            .append(this.getDateFormat(), that.getDateFormat())
            .build();
    }

    /**
     * Gets the current day of the month.
     *
     * @return day of the month
     */
    public abstract int getDay();

    /**
     * Gets the current hour of the day.
     *
     * @return hour of the day
     */
    public abstract int getHour();

    /**
     * Gets the current minute of the hour.
     *
     * @return minute of the hour
     */
    public abstract int getMinute();

    /**
     * Gets the current month of the year.
     *
     * @return month of the year
     */
    public abstract int getMonth();

    /**
     * Gets the current second of the minute.
     *
     * @return second of the minute
     */
    public abstract int getSecond();

    /**
     * Gets the current year.
     *
     * @return current year
     */
    public abstract int getYear();

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getRealTime())
            .append(this.getTimeZone())
            .append(this.getDateFormat())
            .build();
    }

    public final void setTimeZone(@NotNull String timeZoneId) {
        this.setTimeZone(ZoneId.of(timeZoneId, ZoneId.SHORT_IDS));
    }

    public final void setTimeZone(@NotNull ZoneId timeZoneId) {
        this.setTimeZone(TimeZone.getTimeZone(timeZoneId));
    }

    public final void setTimeZone(@NotNull TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Convert date to {@link Date}.
     *
     * @return instance of Date
     */
    public final Date toDate() {
        return Date.from(this.toInstant());
    }

    /**
     * Convert date to {@link Date}.
     *
     * @return instance of Instant
     */
    public final Instant toInstant() {
        return Instant.ofEpochMilli(this.getRealTime());
    }

    /**
     * Convert date to {@link Timestamp}.
     *
     * @return instance of Timestamp
     */
    public final Timestamp toTimestamp() {
        return Timestamp.from(this.toInstant());
    }

    @Override
    public final String toString() {
        return this.getDateFormat()
            .withZone(this.getTimeZone().toZoneId())
            .format(this.toInstant());
    }

}
