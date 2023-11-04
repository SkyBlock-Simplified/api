package dev.sbs.api.client.hypixel.response.skyblock.implementation;

import com.google.common.base.Preconditions;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.date.CustomDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

/**
 * SkyBlock DateTime converter.
 */
public class SkyBlockDate extends CustomDate {

    private static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("MMMMM dd, yyyy HH:mm z");
    public static final ConcurrentList<String> ZOO_CYCLE = Concurrent.newUnmodifiableList("ELEPHANT", "GIRAFFE", "BLUE_WHALE", "TIGER", "LION", "MONKEY");
    public static final ConcurrentList<String> SPECIAL_MAYOR_CYCLE = Concurrent.newUnmodifiableList("SCORPIUS", "DERPY", "JERRY");

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
        this((Length.YEAR_MS * (year - 1)) + (Length.MONTH_MS * (month - 1)) + (Length.DAY_MS * (day - 1)) + (Length.HOUR_MS * hour) + (long) (Length.MINUTE_MS * minute), false);
    }

    public SkyBlockDate(long milliseconds) {
        this(milliseconds, true);
    }

    public SkyBlockDate(long milliseconds, boolean isRealTime) {
        super(isRealTime ? milliseconds : milliseconds + Launch.SKYBLOCK);
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
        if (!(o instanceof SkyBlockDate that)) return false;

        return new EqualsBuilder()
            .append(this.getYear(), that.getYear())
            .append(this.getMonth(), that.getMonth())
            .append(this.getDay(), that.getDay())
            .append(this.getHour(), that.getHour())
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
        return Pair.from(getSpecialMayors(1).iterator().next());
    }

    public static ConcurrentLinkedMap<SkyBlockDate, String> getSpecialMayors(int next) {
        return getSpecialMayors(next, new SkyBlockDate(System.currentTimeMillis()));
    }

    public static ConcurrentLinkedMap<SkyBlockDate, String> getSpecialMayors(int next, SkyBlockDate fromDate) {
        next = Math.max(next, 1);
        SkyBlockDate specialMayorDate = new SkyBlockDate(SkyBlockDate.Launch.SPECIAL_ELECTIONS_START);
        ConcurrentLinkedMap<SkyBlockDate, String> specialMayors = Concurrent.newLinkedMap(next);
        int iterations = 0;

        while (specialMayors.size() < next) {
            specialMayorDate = specialMayorDate.append(8);
            iterations++;

            if (specialMayorDate.getYear() >= fromDate.getYear())
                specialMayors.put(specialMayorDate, SPECIAL_MAYOR_CYCLE.get(iterations % 3));
        }

        return specialMayors;
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
     * {@inheritDoc}
     */
    @Override
    public int getDay() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        remainder -= ((this.getMonth() - 1) * SkyBlockDate.Length.MONTH_MS);
        return (int) (remainder / SkyBlockDate.Length.DAY_MS) + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHour() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        remainder -= ((this.getMonth() - 1) * SkyBlockDate.Length.MONTH_MS);
        remainder -= ((this.getDay() - 1) * SkyBlockDate.Length.DAY_MS);
        return (int) (remainder / SkyBlockDate.Length.HOUR_MS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinute() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        remainder -= ((this.getMonth() - 1) * SkyBlockDate.Length.MONTH_MS);
        remainder -= ((this.getDay() - 1) * SkyBlockDate.Length.DAY_MS);
        remainder -= (this.getHour() * Length.HOUR_MS);
        return (int) (remainder / Length.MINUTE_MS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMonth() {
        long remainder = this.getSkyBlockTime() - ((this.getYear() - 1) * SkyBlockDate.Length.YEAR_MS);
        return (int) (remainder / SkyBlockDate.Length.MONTH_MS) + 1;
    }

    /**
     * Gets the current season of the year.
     *
     * @return season of the year
     */
    public Season getSeason() {
        return Season.values()[this.getMonth() - 1];
    }

    @Override
    public int getSecond() {
        throw new UnsupportedOperationException("Minecraft has no concept of real seconds!");
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
    @Override
    public int getYear() {
        return (int) (this.getSkyBlockTime() / SkyBlockDate.Length.YEAR_MS) + 1;
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
        public static final long MAYOR_ELECTIONS_START = new SkyBlockDate(88, Season.LATE_SUMMER, 27, 0).getRealTime();

        /**
         * The time Mayors end in RealTime.
         */
        public static final long MAYOR_ELECTIONS_END = new SkyBlockDate(88, Season.LATE_SPRING, 27, 0).getRealTime();

        /**
         * The time Special Mayors launched in RealTime.
         */
        public static final long SPECIAL_ELECTIONS_START = new SkyBlockDate(96, Season.LATE_SUMMER, 27, 0).getRealTime();

        /**
         * The time Special Mayors end in RealTime.
         */
        public static final long SPECIAL_ELECTIONS_END = new SkyBlockDate(96, Season.LATE_SPRING, 27, 0).getRealTime();

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
