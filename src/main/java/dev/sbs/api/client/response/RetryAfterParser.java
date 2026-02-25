package dev.sbs.api.client.response;

import dev.sbs.api.util.StringUtil;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Parses Retry-After header from HTTP responses.
 * <p>
 * Supports two formats per RFC 7231:
 * <ul>
 *     <li>Delay-seconds: {@code Retry-After: 120}</li>
 *     <li>HTTP-date: {@code Retry-After: Wed, 21 Oct 2026 07:28:00 GMT}</li>
 * </ul>
 */
@UtilityClass
public final class RetryAfterParser {

    private static final @NotNull String[] RETRY_AFTER_HEADERS = { "Retry-After", "retry-after" };
    private static final @NotNull Pattern RETRY_AFTER_PATTERN = Pattern.compile("^[0-9]+\\.?0*$");
    private static final @NotNull DateFormat RFC822_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);

    /**
     * Parses Retry-After header from a collection of values
     *
     * @param retryAfterValues Collection of header values (typically single value)
     * @return Date when retry should occur, or empty if not present/parseable
     */
    public static @NotNull Optional<Date> parse(@Nullable Collection<String> retryAfterValues) {
        if (retryAfterValues == null || retryAfterValues.isEmpty())
            return Optional.empty();

        String retryAfter = retryAfterValues.iterator().next();

        if (StringUtil.isBlank(retryAfter))
            return Optional.empty();

        return parse(retryAfter.trim());
    }

    /**
     * Parses Retry-After header from headers map
     *
     * @param headers Map of all response headers
     * @return Date when retry should occur, or empty if not present/parseable
     */
    public static @NotNull Optional<Date> parseFromHeaders(@NotNull Map<String, Collection<String>> headers) {
        for (String headerName : RETRY_AFTER_HEADERS) {
            Collection<String> values = headers.get(headerName);
            
            if (values != null && !values.isEmpty())
                return parse(values);
        }
        
        return Optional.empty();
    }

    /**
     * Parses Retry-After header value string
     *
     * @param retryAfter The header value to parse
     * @return Date when retry should occur, or empty if not parseable
     */
    public static @NotNull Optional<Date> parse(@NotNull String retryAfter) {
        // Try parsing as delay-seconds (most common)
        Optional<Date> secondsResult = parseAsSeconds(retryAfter);
        if (secondsResult.isPresent())
            return secondsResult;

        // Try parsing as HTTP-date (RFC 822/1123 format)
        return parseAsHttpDate(retryAfter);
    }

    /**
     * Parses Retry-After as seconds and returns Duration
     *
     * @param retryAfter The header value to parse
     * @return Duration to wait before retry, or empty if not parseable
     */
    public static @NotNull Optional<Duration> parseAsDuration(@NotNull String retryAfter) {
        // Only seconds format can be converted to Duration
        if (RETRY_AFTER_PATTERN.matcher(retryAfter).matches()) {
            String cleaned = retryAfter.replaceAll("\\.0*$", "");
            
            try {
                long seconds = Long.parseLong(cleaned);
                return Optional.of(Duration.ofSeconds(seconds));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        
        return Optional.empty();
    }

    /**
     * Attempts to parse as delay-seconds format
     */
    private static @NotNull Optional<Date> parseAsSeconds(@NotNull String retryAfter) {
        // Match integer or decimal seconds (e.g., "120" or "120.0")
        if (RETRY_AFTER_PATTERN.matcher(retryAfter).matches()) {
            String cleaned = retryAfter.replaceAll("\\.0*$", "");
            
            try {
                long seconds = Long.parseLong(cleaned);
                long deltaMillis = TimeUnit.SECONDS.toMillis(seconds);
                return Optional.of(new Date(System.currentTimeMillis() + deltaMillis));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        
        return Optional.empty();
    }

    /**
     * Attempts to parse as HTTP-date format (RFC 822/1123)
     */
    private static @NotNull Optional<Date> parseAsHttpDate(@NotNull String retryAfter) {
        synchronized (RFC822_FORMAT) {
            try {
                return Optional.of(RFC822_FORMAT.parse(retryAfter));
            } catch (ParseException e) {
                return Optional.empty();
            }
        }
    }

}
