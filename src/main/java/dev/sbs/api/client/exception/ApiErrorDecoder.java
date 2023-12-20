package dev.sbs.api.client.exception;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static java.util.Locale.US;
import static java.util.concurrent.TimeUnit.SECONDS;

public interface ApiErrorDecoder extends ErrorDecoder {

    @Override
    @NotNull ApiException decode(@NotNull String methodKey, @NotNull Response response);

    class Default implements ApiErrorDecoder {

        private final ApiErrorDecoder.RetryAfter retryAfter = new ApiErrorDecoder.RetryAfter();
        private Integer maxBodyBytesLength;
        private Integer maxBodyCharsLength;

        public Default() {
            this(null, null);
        }

        public Default(Integer maxBodyBytesLength, Integer maxBodyCharsLength) {
            this.maxBodyBytesLength = maxBodyBytesLength;
            this.maxBodyCharsLength = maxBodyCharsLength;
        }

        @Override
        public @NotNull ApiException decode(@NotNull String methodKey, @NotNull Response response) {
            FeignException feignException = FeignException.errorStatus(methodKey, response, maxBodyBytesLength, maxBodyCharsLength);
            Date retryAfter = this.retryAfter.apply(firstOrNull(response.headers(), Util.RETRY_AFTER));

            if (retryAfter != null)
                return new ApiRetryableException(feignException, retryAfter);

            return new ApiException(feignException);
        }

        @SuppressWarnings("all")
        private <T> T firstOrNull(Map<String, Collection<T>> map, String key) {
            if (map.containsKey(key) && !map.get(key).isEmpty())
                return map.get(key).iterator().next();

            return null;
        }

    }

    /**
     * Decodes a {@link feign.Util#RETRY_AFTER} header into an absolute date, if possible. <br>
     * See <a href="https://tools.ietf.org/html/rfc2616#section-14.37">Retry-After format</a>
     */
    class RetryAfter {

        static final DateFormat RFC822_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", US);
        private final DateFormat rfc822Format;

        RetryAfter() {
            this(RFC822_FORMAT);
        }

        RetryAfter(@NotNull DateFormat rfc822Format) {
            this.rfc822Format = rfc822Format;
        }

        /**
         * returns a date that corresponds to the first time a request can be retried.
         *
         * @param retryAfter String in
         *        <a href="https://tools.ietf.org/html/rfc2616#section-14.37" >Retry-After format</a>
         */
        public Date apply(String retryAfter) {
            if (retryAfter == null) {
                return null;
            }
            if (retryAfter.matches("^[0-9]+\\.?0*$")) {
                retryAfter = retryAfter.replaceAll("\\.0*$", "");
                long deltaMillis = SECONDS.toMillis(Long.parseLong(retryAfter));
                return new Date(System.currentTimeMillis() + deltaMillis);
            }
            synchronized (rfc822Format) {
                try {
                    return rfc822Format.parse(retryAfter);
                } catch (ParseException ignored) {
                    return null;
                }
            }
        }

    }

}
