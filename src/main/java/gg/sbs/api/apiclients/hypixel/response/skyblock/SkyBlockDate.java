package gg.sbs.api.apiclients.hypixel.response.skyblock;

import com.google.common.base.Preconditions;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;

/**
 * SkyBlock DateTime converter.
 */
public class SkyBlockDate {

    /**
     * The time SkyBlock launched in RealTime.
     */
    public static final long LAUNCH_DATE = 1560275700000L;
    /**
     * The time the Zoo launched in RealTime.
     */
    public static final long ZOO_LAUNCH = LAUNCH_DATE + (Length.YEAR_MS * 66);
    /**
     * The time Jacob relaunched in RealTime.
     */
    public static final long JACOB_LAUNCH = LAUNCH_DATE + (Length.YEAR_MS * 114);

    public static ConcurrentList<String> ZOO_CYCLE = Concurrent.newUnmodifiableList("ELEPHANT", "GIRAFFE", "BLUE_WHALE", "TIGER", "LION", "MONKEY");

    public static final SimpleDateFormat REAL_DATE_FORMAT = new SimpleDateFormat("MM dd yyyy HH:mm");

    /**
     * Get RealTime in milliseconds.
     */
    @Getter private final long realTime;

    public SkyBlockDate(int year, Season season, int day) {
        this(year, (season.ordinal() + 1), day);
    }

    public SkyBlockDate(int year, int month, int day) {
        this(year, month, day, 0);
    }

    public SkyBlockDate(int year, int month, int day, int hour) {
        this((Length.YEAR_MS * (year - 1)) + (Length.MONTH_MS * month) + (Length.DAY_MS * (day - 1)) + (Length.HOUR_MS * hour));
    }

    public SkyBlockDate(long milliseconds) {
        this(milliseconds, false);
    }

    public SkyBlockDate(long milliseconds, boolean isRealTime) {
        if (isRealTime) Preconditions.checkArgument(milliseconds > LAUNCH_DATE, "Milliseconds must be greater than launch date");
        else Preconditions.checkArgument((milliseconds + LAUNCH_DATE) > LAUNCH_DATE, "Milliseconds must be greater than zero");
        this.realTime = isRealTime ? milliseconds : milliseconds + LAUNCH_DATE;
    }

    public SkyBlockDate(Season season, int day, int hour) {
        this.realTime = getRealTime(season, day, hour);
    }

    public static long getRealTime(Season season) {
        return getRealTime(season, 1);
    }

    public static long getRealTime(Season season, int day) {
        return getRealTime(season, day, 1);
    }

    public static long getRealTime(Season season, int day, int hour) {
        Preconditions.checkNotNull(season, "Season cannot be NULL");
        Preconditions.checkArgument(day > 0 && day < 32, "Day must be between 1 and 31 inclusive");
        Preconditions.checkArgument(hour > 0 && hour < 25, "Hour must be between 1 and 24 inclusive");
        long month_millis = (season.ordinal() + 1) * Length.MONTH_MS;
        long day_millis = day * Length.DAY_MS;
        long hour_millis = hour * Length.HOUR_MS;

        return month_millis + day_millis + hour_millis;
    }

    public static long getSkyBlockTime(Season season) {
        return getSkyBlockTime(season, 1);
    }

    public static long getSkyBlockTime(Season season, int day) {
        return getSkyBlockTime(season, day, 1);
    }

    public static long getSkyBlockTime(Season season, int day, int hour) {
        return getRealTime(season, day, hour) - LAUNCH_DATE;
    }

    /**
     * Gets the current day of the month.
     *
     * @return day of the month
     */
    public int getDay() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        remainder = remainder - (this.getMonth() * SkyBlockDate.Length.MONTH_MS);
        return (int)(remainder / SkyBlockDate.Length.DAY_MS) + 1;
    }

    /**
     * Gets the current hour of the day.
     *
     * @return hour of the day
     */
    public int getHour() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        remainder = remainder - (this.getMonth() * SkyBlockDate.Length.MONTH_MS);
        remainder = remainder - ((this.getDay() - 1) * SkyBlockDate.Length.DAY_MS);
        return (int)(remainder / SkyBlockDate.Length.HOUR_MS);
    }

    /**
     * Gets the current month of the year.
     *
     * @return month of the year
     */
    public int getMonth() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        return (int)(remainder / SkyBlockDate.Length.MONTH_MS);
    }

    /**
     * Gets the current season of the year.
     *
     * @return season of the year
     */
    public Season getSeason() {
        return Season.values()[this.getMonth()];
    }

    /**
     * Get RealTime as SkyBlock time.
     *
     * @return skyblock time
     */
    public long getSkyBlockTime() {
        return this.getRealTime() - LAUNCH_DATE;
    }

    /**
     * Gets the number of years for the entire SkyBlock period.
     *
     * @return number of years
     */
    public int getYear() {
        return  (int)(this.getSkyBlockTime() / SkyBlockDate.Length.YEAR_MS) + 1;
    }

    /**
     * Convert SkyBlockDate to Instant.
     *
     * @return instance of Instant
     */
    public Instant toInstant() {
        return Instant.ofEpochMilli(this.getRealTime());
    }

    @Override
    public String toString() {
        return REAL_DATE_FORMAT.format(new Date(this.getRealTime()));
    }

    /**
     * The season of the year, matches up with current month of the year.
     */
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Season {

        EARLY_SPRING("Early Spring"),
        SPRING("Spring"),
        LATE_SPRING("Late Spring"),
        EARLY_SUMMER("Early Summer"),
        SUMMER("Summer"),
        LATE_SUMMER("Late Summer"),
        EARLY_AUTUMN("Early Autumn"),
        AUTUMN("Autumn"),
        LATE_AUTUMN("Late Autumn"),
        EARLY_WINTER("Early Winter"),
        WINTER("Winter"),
        LATE_WINTER("Late Winter");

        @Getter private final String name;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Length {

        public static final long HOURS_TOTAL = 24;
        public static final long DAYS_TOTAL = 31;
        public static final long MONTHS_TOTAL = 12;

        public static final long HOUR_MS = 50000;
        public static final long DAY_MS = HOURS_TOTAL * HOUR_MS; // 1200000
        public static final long MONTH_MS = DAYS_TOTAL * DAY_MS; // 37200000
        public static final long YEAR_MS = MONTHS_TOTAL * MONTH_MS; // 446400000

        public static final long ZOO_CYCLE_MS = YEAR_MS / 2;

    }

    public static class RealTime extends SkyBlockDate {

        public RealTime(long milliseconds) {
            super(milliseconds, true);
        }

    }

    public static class SkyBlockTime extends SkyBlockDate {

        public SkyBlockTime(long milliseconds) {
            super(milliseconds * 1000, false);
        }

    }

}