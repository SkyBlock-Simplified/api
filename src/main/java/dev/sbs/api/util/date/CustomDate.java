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

public abstract class CustomDate {

    private static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Get RealTime in milliseconds.
     */
    @Getter private final long realTime;

    /**
     * The TimeZone used in {@link #toString()}. Defaults to America/New_York.
     */
    @Getter private transient ZoneId zoneId;

    /**
     * The DateTimeFormatter used in {@link #toString()}. Defaults to {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
     */
    @Getter @Setter
    private transient DateTimeFormatter dateFormat;

    protected CustomDate(long realTime) {
        this.realTime = realTime;
        this.dateFormat = DEFAULT_DATE_FORMAT;
        this.zoneId = ZoneId.of("America/New_York");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomDate that = (CustomDate) o;

        return new EqualsBuilder()
            .append(this.getRealTime(), that.getRealTime())
            .append(this.getZoneId(), that.getZoneId())
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
            .append(this.getZoneId())
            .append(this.getDateFormat())
            .build();
    }

    public final void setZoneId(@NotNull String timeZoneId) {
        this.setZoneId(ZoneId.of(timeZoneId, ZoneId.SHORT_IDS));
    }

    public final void setZoneId(@NotNull ZoneId timeZoneId) {
        this.zoneId = timeZoneId;
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
            .withZone(this.getZoneId())
            .format(this.toInstant());
    }

}
