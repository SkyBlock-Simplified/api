package gg.sbs.api.util;

/**
 * Defines the types of compression supported by this library for NBT data.
 */
public enum CompressionType {

    /**
     * No compression.
     */
    NONE,

    /**
     * GZIP compression ({@code GZIPInputStream} and {@code GZIPOutputStream}).
     */
    GZIP,

    /**
     * ZLIB compression ({@code InflaterInputStream} and {@code DeflaterOutputStream}).
     */
    ZLIB

}
