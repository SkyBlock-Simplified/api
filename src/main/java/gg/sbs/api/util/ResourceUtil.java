package gg.sbs.api.util;

import lombok.Cleanup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class ResourceUtil {

	private ResourceUtil() { }

	public static Map<String, String> getEnvironmentVariables() {
		Map<String, String> env = new HashMap<>();
		// Load from src/main/resources/.env
		@Cleanup InputStream file = ResourceUtil.class.getResourceAsStream("../.env");

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

	public static InputStream getResource(String resourcePath) {
		return ResourceUtil.class.getResourceAsStream(resourcePath);
	}

	public static void saveResource(File outputDir, String resourcePath, boolean replace) {
		saveResource(outputDir, resourcePath, "", replace);
	}

	public static void saveResource(File outputDir, String resourcePath, String child, boolean replace) {
		File directory = outputDir;

		if (StringUtil.notEmpty(child))
			directory = new File(directory, child);

		File output = new File(directory, resourcePath);

		try (InputStream inputStream = getResource(resourcePath)) {
			if (!directory.exists()) {
				if (!directory.mkdirs())
					throw new IllegalStateException(StringUtil.format("Unable to create parent directories for ''{0}''.", output));
			}

			if (replace)
				output.delete();
			else if (output.exists())
				throw new IllegalStateException(StringUtil.format("Output file ''{0}'' already exists.", output));

			try (FileOutputStream outputStream = new FileOutputStream(output)) {
				byte[] buffer = new byte[1024];
				int length;

				while ((length = inputStream.read(buffer)) > 0)
					outputStream.write(buffer, 0, length);
			}
		} catch (Exception exception) {
			throw new IllegalStateException(StringUtil.format("Unable to save resource ''{0}'' to ''{1}''.", resourcePath, output), exception);
		}
	}

}