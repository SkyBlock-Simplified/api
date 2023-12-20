package dev.sbs.api.client.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum HttpMethod {

    GET,
    HEAD,
    POST(true),
    PUT(true),
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE,
    PATCH(true);

    private final boolean withBody;

    HttpMethod() {
        this(false);
    }

    public static @NotNull HttpMethod of(@NotNull String name) {
        return Arrays.stream(values())
            .filter(value -> value.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(GET);
    }

}
