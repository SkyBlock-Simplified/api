package dev.sbs.api.util;

import dev.sbs.api.builder.EqualsBuilder;
import dev.sbs.api.builder.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Getter
public class SimpleDate {

    private static final @NotNull DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Get RealTime in milliseconds.
     */
    private final long realTime;

    /**
     * The TimeZone used in {@link #toCalendar()}, {@link #toLocalISO()} and {@link #toString()}.
     * <p>
     * Defaults to America/New_York.
     */
    private transient @NotNull ZoneId zoneId;

    /**
     * The DateTimeFormatter used in {@link #toLocalISO()} and {@link #toString()}.
     * <p>
     * Defaults to {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
     */
    @Setter
    private transient @NotNull DateTimeFormatter dateFormat;

    public SimpleDate(@NotNull String duration) {
        this(System.currentTimeMillis() + getUnixDuration(duration));
    }

    public SimpleDate(@NotNull Instant instant) {
        this(instant.toEpochMilli());
    }

    public SimpleDate(long realTime) {
        this.realTime = realTime;
        this.dateFormat = DEFAULT_DATE_FORMAT;
        this.zoneId = ZoneId.of("America/New_York");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleDate that = (SimpleDate) o;

        return new EqualsBuilder()
            .append(this.getRealTime(), that.getRealTime())
            .append(this.getZoneId(), that.getZoneId())
            .append(this.getDateFormat(), that.getDateFormat())
            .build();
    }

    /**
     * Gets the millisecond duration of {@code dateTime}. Months is unsupported, use days.
     * <p>
     * Valid values are: {@code 1y2w3d4h5m6s - 1 year 2 weeks 3 days 4 hours 5 minutes 6 seconds}
     *
     * @param duration to convert
     * @return milliseconds based on {@code time}
     */
    public static long getUnixDuration(@NotNull String duration) {
        duration = StringUtil.stripToEmpty(duration);
        long durationMillis = 0;
        long component = 0;

        if (StringUtil.isNotEmpty(duration)) {
            for (int i = 0; i < duration.length(); i++) {
                char chr = duration.charAt(i);

                if (Character.isDigit(chr)) {
                    component *= 10;
                    component += chr - '0';
                } else {
                    switch (Character.toLowerCase(chr)) {
                        case 'y':
                            component *= 52;
                        case 'w':
                            component *= 7;
                        case 'd':
                            component *= 24;
                        case 'h':
                            component *= 60;
                        case 'm':
                            component *= 60;
                        case 's':
                            component *= 1000;
                    }

                    durationMillis += component;
                    component = 0;
                }
            }
        }

        return durationMillis;
    }

    /**
     * Gets the current day of the month.
     *
     * @return day of the month
     */
    public int getDay() {
        return this.toCalendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Gets the current hour of the day.
     *
     * @return hour of the day
     */
    public int getHour() {
        return this.toCalendar().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Gets the current minute of the hour.
     *
     * @return minute of the hour
     */
    public int getMinute() {
        return this.toCalendar().get(Calendar.MINUTE);
    }

    /**
     * Gets the current month of the year.
     *
     * @return month of the year
     */
    public int getMonth() {
        return this.toCalendar().get(Calendar.MONTH);
    }

    /**
     * Gets the current second of the minute.
     *
     * @return second of the minute
     */
    public int getSecond() {
        return this.toCalendar().get(Calendar.SECOND);
    }

    /**
     * Gets the current year.
     *
     * @return current year
     */
    public int getYear() {
        return this.toCalendar().get(Calendar.YEAR);
    }

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
     * Convert date and time to {@link Calendar}.
     */
    public final @NotNull Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getRealTime());
        calendar.setTimeZone(TimeZone.getTimeZone(this.getZoneId()));
        return calendar;
    }

    /**
     * Convert date and time to {@link Date}.
     */
    public final @NotNull Date toDate() {
        return Date.from(this.toInstant());
    }

    /**
     * Convert date and time to {@link Instant}.
     */
    public final @NotNull Instant toInstant() {
        return Instant.ofEpochMilli(this.getRealTime());
    }

    public final @NotNull String toLocalISO() {
        return this.getDateFormat()
            .withZone(this.getZoneId())
            .format(this.toInstant());
    }

    /**
     * Convert date and time to {@link Timestamp}.
     */
    public final @NotNull Timestamp toTimestamp() {
        return Timestamp.from(this.toInstant());
    }

    @Override
    public final @NotNull String toString() {
        return this.toLocalISO();
    }

}
