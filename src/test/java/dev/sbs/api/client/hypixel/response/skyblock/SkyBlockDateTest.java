package dev.sbs.api.client.hypixel.response.skyblock;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class SkyBlockDateTest {

    @Test
    public void getDate_ok() {
        SkyBlockDate sbDate = new SkyBlockDate(System.currentTimeMillis(), true);
        SkyBlockDate sbDate2 = new SkyBlockDate(sbDate.getYear(), sbDate.getMonth(), sbDate.getDay(), sbDate.getHour());

        System.out.println("SB Time #1: " + sbDate.getSkyBlockTime());
        System.out.println("Year #1: " + sbDate.getYear());
        System.out.println("Month #1: " + sbDate.getMonth());
        System.out.println("Day #1: " + sbDate.getDay());
        System.out.println("Hour #1: " + sbDate.getHour());
        System.out.println("Season #1: " + sbDate.getSeason().getName());

        System.out.println();

        System.out.println("SB Time #2: " + sbDate2.getSkyBlockTime());
        System.out.println("Year #2: " + sbDate2.getYear());
        System.out.println("Month #2: " + sbDate2.getMonth());
        System.out.println("Day #2: " + sbDate2.getDay());
        System.out.println("Hour #2: " + sbDate2.getHour());
        System.out.println("Season #2: " + sbDate2.getSeason().getName());

        MatcherAssert.assertThat(sbDate.getYear(), Matchers.equalTo(sbDate2.getYear()));
        MatcherAssert.assertThat(sbDate.getMonth(), Matchers.equalTo(sbDate2.getMonth()));
        MatcherAssert.assertThat(sbDate.getDay(), Matchers.equalTo(sbDate2.getDay()));
        MatcherAssert.assertThat(sbDate.getHour(), Matchers.equalTo(sbDate2.getHour()));
    }

}
