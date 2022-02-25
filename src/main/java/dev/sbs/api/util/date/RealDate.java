package dev.sbs.api.util.date;

import java.util.Calendar;

public class RealDate extends CustomDate {

    public RealDate(long realTime) {
        super(realTime);
    }

    protected final Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getRealTime());
        return calendar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getDay() {
        return this.getCalendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getHour() {
        return this.getCalendar().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getMinute() {
        return this.getCalendar().get(Calendar.MINUTE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getMonth() {
        return this.getCalendar().get(Calendar.MONTH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getSecond() {
        return this.getCalendar().get(Calendar.SECOND);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getYear() {
        return this.getCalendar().get(Calendar.YEAR);
    }

}
