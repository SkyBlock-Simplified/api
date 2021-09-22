package gg.sbs.api.util;

import com.google.common.base.Preconditions;
import net.minecraft.client.Minecraft;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public abstract class CSVStorage {

	private static final File MINECRAFT_CONFIG = new File(Minecraft.getMinecraft().gameDir, "config");
	private static final Pattern CSV_SPLIT = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	private final File file;

	public CSVStorage(String fileName) {
		Preconditions.checkArgument(StringUtil.notEmpty(fileName), "Filename cannot be NULL!");
		this.file = new File(MINECRAFT_CONFIG, fileName + (fileName.endsWith(".csv") ? "" : ".csv"));

		try {
			this.reload();
		} catch (IOException ioex) {
			throw new IllegalStateException(StringUtil.format("Unable to load ''{0}''!", this.getLocalFile().getName()), ioex);
		}
	}

	protected final ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}

	public final List<String> getLines() throws IOException {
		try (InputStreamReader inputStream = (this.getLocalFile().exists() ? new FileReader(this.getLocalFile()) : new InputStreamReader(this.getResource()))) {
			try (BufferedReader reader = new BufferedReader(inputStream)) {
				ConcurrentList<String> lines = Concurrent.newList();
				String line;

				while ((line = reader.readLine()) != null)
					lines.add(line);

				return Collections.unmodifiableList(lines);
			}
		}
	}

	protected final InputStream getResource() {
		URL url = this.getClassLoader().getResource(this.getLocalFile().getName());

		if (url != null) {
			try {
				URLConnection connection = url.openConnection();
				connection.setUseCaches(false);
				return connection.getInputStream();
			} catch (IOException ignore) { }
		}

		throw new IllegalArgumentException(StringUtil.format("No resource with name ''{0}'' found!"));
	}

	protected final File getLocalFile() {
		return this.file;
	}

	protected abstract void preReload();

	protected abstract void processLine(String[] parts);

	public final void reload() throws IOException {
		try (InputStreamReader inputStream = (this.getLocalFile().exists() ? new FileReader(this.getLocalFile()) : new InputStreamReader(this.getResource()))) {
			try (BufferedReader reader = new BufferedReader(inputStream)) {
				String line;

				while ((line = reader.readLine()) != null) {
					if (StringUtil.isEmpty(line) || line.charAt(0) == '#') continue;
					this.processLine(CSV_SPLIT.split(line));
				}
			}
		}
	}

	public final void save() {
		this.save(false);
	}

	public final void save(boolean replace) {
		try (InputStream inputStream = this.getResource()) {
			File parent = this.file.getAbsoluteFile().getParentFile();

			if (!parent.exists()) {
				if (!parent.mkdirs())
					throw new IllegalStateException(StringUtil.format("Unable to create parent directories for ''{0}''!", this.getLocalFile()));
			}

			if (this.getLocalFile().exists() && !replace)
				throw new IllegalStateException("File already exists!");

			this.getLocalFile().delete();

			try (FileOutputStream outputStream = new FileOutputStream(this.getLocalFile())) {
				byte[] buffer = new byte[1024];
				int length;

				while ((length = inputStream.read(buffer)) > 0)
					outputStream.write(buffer, 0, length);
			}
		} catch (Exception ex) {
			throw new IllegalStateException(StringUtil.format("Unable to save resource ''{0}'' to ''{1}''!", ex, this.getLocalFile().getName(), this.getLocalFile()));
		}
	}

}