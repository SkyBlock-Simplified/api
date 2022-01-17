package dev.sbs.api.util;

public class LogTime {

    private static long lastUpdated = 0;

    public static void log(String line) {
        System.out.println(line + " (" + (lastUpdated == 0 ? 0 : (System.currentTimeMillis() - lastUpdated)) + "ms)");
        lastUpdated = System.currentTimeMillis();
    }

}
