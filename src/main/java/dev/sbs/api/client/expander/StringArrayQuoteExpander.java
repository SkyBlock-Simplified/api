package dev.sbs.api.client.expander;

import feign.Param;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class StringArrayQuoteExpander implements Param.Expander {

    @Override
    public String expand(@NotNull Object value) {
        if (String[].class.isAssignableFrom(value.getClass())) {
            return Arrays.stream((String[]) value)
                .map(str -> String.format("\"%s\"", str))
                .collect(Collectors.joining(","));
        }

        return String.valueOf(value);
    }

}
