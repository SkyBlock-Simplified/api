package dev.sbs.api.util;

import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.data.tuple.pair.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class SkyBlockDateTest {

    @Test
    public void getDate_ok() {
        SkyBlockDate currentDate = new SkyBlockDate(System.currentTimeMillis(), true);

        long currentYear = currentDate.getYear();
        long currentMonth = currentDate.getMonth();
        SkyBlockDate.Season currentSeason = currentDate.getSeason();
        long currentDay = currentDate.getDay();
        long currentHour = currentDate.getHour();
        long currentMinute = currentDate.getMinute();
        SkyBlockDate sbDate2 = new SkyBlockDate(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay(), currentDate.getHour(), currentDate.getMinute());

        SkyBlockDate futureDate = new SkyBlockDate(300, 0, 0);
        long remaining = futureDate.getRealTime() - currentDate.getRealTime();
        long seconds = remaining / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        long days = hours / 24;
        hours %= 24;
        minutes %= 60;
        seconds %= 60;

        Pair<SkyBlockDate, String> nextSpecialMayor = SkyBlockDate.getNextSpecialMayor();
        ConcurrentLinkedMap<SkyBlockDate, String> specialMayors = SkyBlockDate.getSpecialMayors(5, new SkyBlockDate(System.currentTimeMillis()).append(-16));
        int specialYear = nextSpecialMayor.getLeft().getYear();

        System.out.println("SB Time #1: " + currentDate.getSkyBlockTime());
        System.out.println("Year #1: " + currentDate.getYear());
        System.out.println("Month #1: " + currentDate.getMonth());
        System.out.println("Day #1: " + currentDate.getDay());
        System.out.println("Hour #1: " + currentDate.getHour());
        System.out.println("Minute #1: " + currentDate.getMinute());
        System.out.println("Season #1: " + currentDate.getSeason().getName());

        System.out.println();

        System.out.println("SB Time #2: " + sbDate2.getSkyBlockTime());
        System.out.println("Year #2: " + sbDate2.getYear());
        System.out.println("Month #2: " + sbDate2.getMonth());
        System.out.println("Day #2: " + sbDate2.getDay());
        System.out.println("Hour #2: " + sbDate2.getHour());
        System.out.println("Minute #2: " + sbDate2.getMinute());
        System.out.println("Season #2: " + sbDate2.getSeason().getName());

        MatcherAssert.assertThat(currentDate, Matchers.equalTo(sbDate2));
    }

}
