package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.common.base.Preconditions;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.tuple.Pair;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * SkyBlock DateTime converter.
 */
public class SkyBlockDate {

    @Getter
    private static final ConcurrentList<String> zooCycle = Concurrent.newUnmodifiableList("ELEPHANT", "GIRAFFE", "BLUE_WHALE", "TIGER", "LION", "MONKEY");
    @Getter
    private static final ConcurrentList<String> specialMayorCycle = Concurrent.newUnmodifiableList("SCORPIUS", "DERPY", "JERRY");
    private static final SimpleDateFormat realDateFormat = new SimpleDateFormat("MMMMM dd, yyyy HH:mm z");

    /**
     * Get RealTime in milliseconds.
     */
    @Getter
    private final long realTime;

    public SkyBlockDate(Season season, int day) {
        this(season, day, 0);
    }

    public SkyBlockDate(Season season, int day, int hour) {
        this(season, day, hour, 0);
    }

    public SkyBlockDate(Season season, int day, int hour, int minute) {
        this(getRealTime(season, day, hour, minute), false);
    }

    public SkyBlockDate(int year, Season season, int day) {
        this(year, (season.ordinal() + 1), day);
    }

    public SkyBlockDate(int year, int month, int day) {
        this(year, month, day, 0);
    }

    public SkyBlockDate(int year, Season season, int day, int hour) {
        this(year, season, day, hour, 0);
    }

    public SkyBlockDate(int year, Season season, int day, int hour, int minute) {
        this(year, (season.ordinal() + 1), day, hour, minute);
    }

    public SkyBlockDate(int year, int month, int day, int hour) {
        this(year, month, day, hour, 0);
    }

    public SkyBlockDate(int year, int month, int day, int hour, int minute) {
        this((Length.YEAR_MS * (year - 1)) + (Length.MONTH_MS * month) + (Length.DAY_MS * (day - 1)) + (Length.HOUR_MS * hour) + (long) (Length.MINUTE_MS * (minute - 1)), false);
    }

    public SkyBlockDate(long milliseconds) {
        this(milliseconds, true);
    }

    public SkyBlockDate(long milliseconds, boolean isRealTime) {
        if (isRealTime) Preconditions.checkArgument(milliseconds > Launch.SKYBLOCK, "Milliseconds must be greater than launch date.");
        else Preconditions.checkArgument((milliseconds + Launch.SKYBLOCK) > Launch.SKYBLOCK, "Milliseconds must be greater than zero.");
        this.realTime = isRealTime ? milliseconds : milliseconds + Launch.SKYBLOCK;
    }

    public SkyBlockDate append(int year) {
        return this.append(year, 0);
    }

    public SkyBlockDate append(int year, Season season) {
        return this.append(year, season.ordinal() + 1);
    }

    public SkyBlockDate append(int year, int month) {
        return this.append(year, month, 0);
    }

    public SkyBlockDate append(int year, Season season, int day) {
        return this.append(year, season.ordinal() + 1, day);
    }

    public SkyBlockDate append(int year, int month, int day) {
        return this.append(year, month, day, 0);
    }

    public SkyBlockDate append(int year, Season season, int day, int hour) {
        return this.append(year, season.ordinal() + 1, day, hour);
    }

    public SkyBlockDate append(int year, int month, int day, int hour) {
        return new SkyBlockDate(this.getYear() + year, this.getMonth() + month, this.getDay() + day, this.getHour() + hour);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkyBlockDate)) return false;
        SkyBlockDate that = (SkyBlockDate) o;

        return new EqualsBuilder()
            .append(this.getYear(), that.getYear())
            .append(this.getMonth(), that.getMonth())
            .append(this.getDay(), that.getDay())
            .append(this.getHour(), that.getHour())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getYear())
            .append(this.getMonth())
            .append(this.getDay())
            .append(this.getHour())
            .build();
    }

    public static long getRealTime(Season season) {
        return getRealTime(season, 1);
    }

    public static long getRealTime(Season season, int day) {
        return getRealTime(season, day, 1);
    }

    public static long getRealTime(Season season, int day, int hour) {
        return getRealTime(season, day, hour, 0);
    }

    public static long getRealTime(Season season, int day, int hour, int minute) {
        Preconditions.checkNotNull(season, "Season cannot be NULL.");
        Preconditions.checkArgument(day > 0 && day < 32, "Day must be between 1 and 31 inclusive.");
        Preconditions.checkArgument(hour > 0 && hour < 25, "Hour must be between 1 and 24 inclusive.");
        long month_millis = (season.ordinal() + 1) * Length.MONTH_MS;
        long day_millis = day * Length.DAY_MS;
        long hour_millis = hour * Length.HOUR_MS;
        long minute_millis = (long) (minute * Length.MINUTE_MS);

        return month_millis + day_millis + hour_millis - minute_millis;
    }

    public static Pair<SkyBlockDate, String> getNextSpecialMayor() {
        SkyBlockDate currentDate = new SkyBlockDate(System.currentTimeMillis());
        SkyBlockDate nextSpecialMayor = new SkyBlockDate(SkyBlockDate.Launch.SPECIAL_ELECTIONS);
        int iterations = 0;

        while (nextSpecialMayor.getYear() < currentDate.getYear()) {
            nextSpecialMayor = nextSpecialMayor.append(8);
            iterations++;
        }

        return Pair.of(nextSpecialMayor, getSpecialMayorCycle().get(iterations % 3));
    }

    public static long getSkyBlockTime(Season season) {
        return getSkyBlockTime(season, 1);
    }

    public static long getSkyBlockTime(Season season, int day) {
        return getSkyBlockTime(season, day, 1);
    }

    public static long getSkyBlockTime(Season season, int day, int hour) {
        return getSkyBlockTime(season, day, hour, 0);
    }

    public static long getSkyBlockTime(Season season, int day, int hour, int minute) {
        return getRealTime(season, day, hour, minute) - Launch.SKYBLOCK;
    }

    /**
     * Gets the current day of the month.
     *
     * @return day of the month
     */
    public int getDay() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        remainder = remainder - (this.getMonth() * SkyBlockDate.Length.MONTH_MS);
        return (int) (remainder / SkyBlockDate.Length.DAY_MS) + 1;
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
        return (int) (remainder / SkyBlockDate.Length.HOUR_MS);
    }

    /**
     * Gets the current minute of the day.
     *
     * @return minute of the day
     */
    public int getMinute() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        remainder = remainder - (this.getMonth() * SkyBlockDate.Length.MONTH_MS);
        remainder = remainder - ((this.getDay() - 1) * SkyBlockDate.Length.DAY_MS);
        remainder = remainder - (this.getHour() * Length.HOUR_MS);
        return (int) (remainder / Length.MINUTE_MS) + 1;
    }

    /**
     * Gets the current month of the year.
     *
     * @return month of the year
     */
    public int getMonth() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        return (int) (remainder / SkyBlockDate.Length.MONTH_MS);
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
        return this.getRealTime() - Launch.SKYBLOCK;
    }

    /**
     * Gets the number of years for the entire SkyBlock period.
     *
     * @return number of years
     */
    public int getYear() {
        return (int) (this.getSkyBlockTime() / SkyBlockDate.Length.YEAR_MS) + 1;
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
        return realDateFormat.format(new Date(this.getRealTime()));
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

        @Getter
        private final String name;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Launch {

        /**
         * The time SkyBlock launched in RealTime.
         */
        public static final long SKYBLOCK = 1560275700000L;

        /**
         * The time the Zoo launched in RealTime.
         */
        public static final long ZOO = SKYBLOCK + (Length.YEAR_MS * 66);

        /**
         * The time Jacob relaunched in RealTime.
         */
        public static final long JACOB = SKYBLOCK + (Length.YEAR_MS * 114);

        /**
         * The time Mayors launched in RealTime.
         */
        public static final long MAYOR_ELECTIONS = new SkyBlockDate(88, Season.LATE_SUMMER, 27, 0).getRealTime();

        /**
         * The time Special Mayors launched in RealTime.
         */
        public static final long SPECIAL_ELECTIONS = new SkyBlockDate(96, Season.LATE_SUMMER, 27, 0).getRealTime();

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Length {

        public static final long MINUTES_TOTAL = 60;
        public static final long HOURS_TOTAL = 24;
        public static final long DAYS_TOTAL = 31;
        public static final long MONTHS_TOTAL = 12;

        public static final double MINUTE_MS = 50000.0 / 60;
        public static final long HOUR_MS = (long) (MINUTES_TOTAL * MINUTE_MS);
        public static final long DAY_MS = HOURS_TOTAL * HOUR_MS; // 1200000
        public static final long MONTH_MS = DAYS_TOTAL * DAY_MS; // 37200000
        public static final long YEAR_MS = MONTHS_TOTAL * MONTH_MS; // 446400000

        public static final long ZOO_CYCLE_MS = YEAR_MS / 2; // 223200000

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
