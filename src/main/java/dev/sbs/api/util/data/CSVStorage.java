package dev.sbs.api.util.data;

import com.google.common.base.Preconditions;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.ResourceUtil;
import dev.sbs.api.util.helper.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public abstract class CSVStorage {

	private static final Pattern CSV_SPLIT = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	private final File file;

	public CSVStorage(File folder, String fileName) {
		Preconditions.checkArgument(StringUtil.isNotEmpty(fileName), "Filename cannot be NULL!");
		this.file = new File(folder, fileName + (fileName.endsWith(".csv") ? "" : ".csv"));

		try {
			this.reload();
		} catch (IOException ioex) {
			throw new IllegalStateException(String.format("Unable to load '%s'!", this.getLocalFile().getName()), ioex);
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
		return ResourceUtil.getResource(this.getLocalFile().getName());
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
		ResourceUtil.saveResource(this.getLocalFile().getParentFile(), this.getLocalFile().getName(), replace);
	}

}
