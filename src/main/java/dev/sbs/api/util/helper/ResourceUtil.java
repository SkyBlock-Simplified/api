package dev.sbs.api.util.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceUtil {

    public static Map<String, String> getEnvironmentVariables() {
        Map<String, String> env = new HashMap<>();
        // Load from src/main/resources/.env
        InputStream file = ResourceUtil.class.getResourceAsStream("../.env");
        if (file != null) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("=")) {
                    String[] pair = line.split("=");
                    env.put(pair[0], pair.length == 2 ? pair[1] : "");
                }
            }
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Load from OS
        env.putAll(System.getenv());
        return env;
    }

    public static String getEnvironmentVariable(String variableName) {
        return getEnvironmentVariables().get(variableName);
    }

    public static String getEnvironmentVariable(String variableName, String defaultValue) {
        return getEnvironmentVariables().getOrDefault(variableName, defaultValue);
    }

    public static InputStream getResource(String resourcePath) {
        return ResourceUtil.class.getResourceAsStream(resourcePath);
    }

    public static void saveResource(File outputDir, String resourcePath, boolean replace) {
        saveResource(outputDir, resourcePath, "", replace);
    }

    public static void saveResource(File outputDir, String resourcePath, String child, boolean replace) {
        File directory = outputDir;

        if (StringUtil.isNotEmpty(child))
            directory = new File(directory, child);

        File output = new File(directory, resourcePath);

        try (InputStream inputStream = getResource(resourcePath)) {
            if (!directory.exists()) {
                if (!directory.mkdirs())
                    throw new IllegalStateException(FormatUtil.format("Unable to create parent directories for ''{0}''.", output));
            }

            if (replace)
                output.delete();
            else if (output.exists())
                throw new IllegalStateException(FormatUtil.format("Output file ''{0}'' already exists.", output));

            try (FileOutputStream outputStream = new FileOutputStream(output)) {
                byte[] buffer = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, length);
            }
        } catch (Exception exception) {
            throw new IllegalStateException(FormatUtil.format("Unable to save resource ''{0}'' to ''{1}''.", resourcePath, output), exception);
        }
    }

}
