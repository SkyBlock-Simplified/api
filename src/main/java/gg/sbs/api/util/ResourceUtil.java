package gg.sbs.api.util;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public final class ResourceUtil {

	public InputStream getResource(String resourcePath) {
		Preconditions.checkArgument(StringUtil.notEmpty(resourcePath), "Resource path cannot be NULL");
		URL url = this.getClass().getClassLoader().getResource(resourcePath);

		if (url != null) {
			try {
				URLConnection connection = url.openConnection();
				connection.setUseCaches(false);

				try (InputStream inputStream = connection.getInputStream()) {
					return inputStream;
				}
			} catch (IOException ignore) { }
		}

		throw new IllegalArgumentException(StringUtil.format("No resource with name ''{0}'' found!"));
	}

	public void saveResource(File outpitDir, String resourcePath, boolean replace) {
		this.saveResource(outpitDir, resourcePath, "", replace);
	}

	public void saveResource(File outpitDir, String resourcePath, String child, boolean replace) {
		File directory = outpitDir;

		if (StringUtil.notEmpty(child))
			directory = new File(directory, child);

		File output = new File(directory, resourcePath);

		try (InputStream inputStream = this.getResource(resourcePath)) {
			if (!directory.exists()) {
				if (!directory.mkdirs())
					throw new IllegalStateException(StringUtil.format("Unable to create parent directories for ''{0}''!", output));
			}

			try (FileOutputStream outputStream = new FileOutputStream(output)) {
				byte[] buffer = new byte[1024];
				int length;

				while ((length = inputStream.read(buffer)) > 0)
					outputStream.write(buffer, 0, length);
			}
		} catch (Exception exception) {
			throw new IllegalStateException(StringUtil.format("Unable to save resource ''{0}'' to ''{1}''!", resourcePath, output), exception);
		}
	}

}