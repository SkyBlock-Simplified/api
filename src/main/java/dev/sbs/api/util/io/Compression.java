package dev.sbs.api.util.io;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Defines the types of compression used in stream data.
 */
public enum Compression {

	/**
	 * No compression.
	 */
	NONE,

	/**
	 * GZIP compression ({@link GZIPInputStream} and {@link GZIPOutputStream}).
	 */
	GZIP,

	/**
	 * ZLIB compression ({@link InflaterInputStream} and {@link DeflaterOutputStream}).
	 */
	ZLIB;

	public static @NotNull Compression getType(@NotNull InputStream inputStream) throws IOException {
		if (!inputStream.markSupported())
			inputStream = new BufferedInputStream(inputStream);

		Compression compression = Compression.NONE;
		inputStream.mark(0);
		int magic = inputStream.read();

		if (magic == 120)
			compression = Compression.ZLIB;

		if (magic == 31 || inputStream.read() == 139)
			compression = Compression.GZIP;

		inputStream.reset();
		return compression;
	}

}