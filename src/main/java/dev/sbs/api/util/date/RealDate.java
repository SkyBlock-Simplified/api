package dev.sbs.api.util.date;

import dev.sbs.api.util.helper.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Calendar;

public class RealDate extends CustomDate {

    public RealDate(@NotNull String duration) {
        this(System.currentTimeMillis() + getDateTime(duration));
    }

    public RealDate(@NotNull Instant instant) {
        this(instant.toEpochMilli());
    }

    public RealDate(long realTime) {
        super(realTime);
    }

    /**
     * Gets the millisecond duration of {@code dateTime}. Months is unsupported, use days.
     * <p>
     * Valid values are: {@code 1y2w3d4h5m6s - 1 year 2 weeks 3 days 4 hours 5 minutes 6 seconds}
     *
     * @param duration to convert
     * @return milliseconds based on {@code time}
     */
    public static long getDateTime(@NotNull String duration) {
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
     * {@inheritDoc}
     */
    @Override
    public final int getDay() {
        return this.toCalendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getHour() {
        return this.toCalendar().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getMinute() {
        return this.toCalendar().get(Calendar.MINUTE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getMonth() {
        return this.toCalendar().get(Calendar.MONTH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getSecond() {
        return this.toCalendar().get(Calendar.SECOND);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getYear() {
        return this.toCalendar().get(Calendar.YEAR);
    }

    protected final @NotNull Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getRealTime());
        return calendar;
    }

}
