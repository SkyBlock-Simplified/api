package dev.sbs.api.client.hypixel.response.hypixel;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public class HypixelPunishmentStatsResponse {

    private boolean success;
    @SerializedName("watchdog_lastMinute")
    private int watchdogLastMinute;
    @SerializedName("staff_rollingDaily")
    private int staffRollingDaily;
    @SerializedName("watchdog_total")
    private int watchdogTotal;
    @SerializedName("watchdog_rollingDaily")
    private int watchdogRollingDaily;
    @SerializedName("staff_total")
    private int staffTotal;

    public Staff getStaff() {
        return new Staff(this.staffRollingDaily, this.staffTotal);
    }

    public Watchdog getWatchdog() {
        return new Watchdog(this.watchdogRollingDaily, this.watchdogTotal, this.watchdogLastMinute);
    }

    public boolean isSuccess() {
        return this.success;
    }

    public static class Staff {

        @Getter
        private final int rollingDaily;
        @Getter
        private final int total;

        private Staff(int rollingDaily, int total) {
            this.rollingDaily = rollingDaily;
            this.total = total;
        }

    }

    public static class Watchdog {

        @Getter
        private final int rollingDaily;
        @Getter
        private final int total;
        @Getter
        private final int lastMinute;

        private Watchdog(int rollingDaily, int total, int lastMinute) {
            this.rollingDaily = rollingDaily;
            this.total = total;
            this.lastMinute = lastMinute;
        }

    }

}
