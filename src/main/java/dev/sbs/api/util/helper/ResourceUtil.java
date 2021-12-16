package dev.sbs.api.util.helper;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceUtil {

    public static Map<String, String> getEnvironmentVariables() {
        Map<String, String> env = new HashMap<>();

        // Load src/main/resources/.env
        InputStream file = getResource("../.env");

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

        // Override From OS
        env.putAll(System.getenv());
        return env;
    }

    /**
     * Get environment variable.
     *
     * @param variableName the name of the environment variable
     * @return the value of the environment variable
     */
    public static Optional<String> getEnv(String variableName) {
        return getEnvironmentVariables()
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().equalsIgnoreCase(variableName))
            .map(Map.Entry::getValue)
            .findFirst();
    }

    public static InputStream getResource(String resourcePath) {
        resourcePath = RegexUtil.replaceFirst(resourcePath, "^resources/", "");
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        return inputStream == null ? ResourceUtil.class.getResourceAsStream(resourcePath) : inputStream;
    }

    public static List<String> getResourceFiles(String resourcePath) {
        List<String> fileNames = new ArrayList<>();

        try {
            resourcePath = RegexUtil.replaceFirst(resourcePath, "^resources/", "");
            @Cleanup InputStream inputStream = getResource(resourcePath);
            @Cleanup BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String resource;

            while ((resource = bufferedReader.readLine()) != null)
                fileNames.add(resource);
        } catch (IOException ignore) { }

        return fileNames;
    }

    public static void saveResource(File outputDir, String resourcePath, boolean replace) {
        saveResource(outputDir, resourcePath, "", replace);
    }

    @SuppressWarnings("all")
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
