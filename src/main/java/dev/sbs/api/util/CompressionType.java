package dev.sbs.api.util;

/**
 * Defines the types of compression used in stream data.
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
