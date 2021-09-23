package gg.sbs.api.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EnvVars {
    public static Map<String, String> get() {
        Map<String, String> env = new HashMap<>();
        // Load from src/main/resources/.env
        InputStream file = EnvVars.class.getResourceAsStream("../.env");
        if (file != null) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("=")) {
                    String[] pair = line.split("=");
                    env.put(pair[0], pair.length == 2 ? pair[1] : "");
                }
            }
        }
        // Load from OS
        env.putAll(System.getenv());
        return env;
    }
}
