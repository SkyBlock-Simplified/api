package dev.sbs.api.client.response;

import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

@Getter
public enum HttpState {

    CLIENT_ERROR,
    JAVA_ERROR,
    INFORMATIONAL,
    OTHER,
    NETWORK_ERROR,
    REDIRECTION,
    SERVER_ERROR,
    CLOUDFLARE_ERROR("Cloudflare"),
    NGINX_ERROR("nginx"),
    SUCCESS;

    private final String title;

    HttpState() {
        this(null);
    }

    HttpState(String title) {
        this.title = StringUtil.isEmpty(title) ? StringUtil.capitalizeFully(this.name().replace("_", " ")) : title;
    }

}
