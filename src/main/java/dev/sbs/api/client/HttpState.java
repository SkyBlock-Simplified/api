package dev.sbs.api.client;

import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

public enum HttpState {

    CLIENT_ERROR,
    JAVA_ERROR,
    INFORMATIONAL,
    OTHER,
    NETWORK_ERROR,
    REDIRECTION,
    SERVER_ERROR,
    CLOUDFLARE_ERROR("CloudFlare"),
    NGINX_ERROR("nginx"),
    SUCCESS;

    @Getter private final String title;

    HttpState() {
        this(null);
    }

    HttpState(String title) {
        this.title = StringUtil.isEmpty(title) ? StringUtil.capitalizeFully(this.name().replace("_", " ")) : title;
    }

}
