package gg.sbs.api.apiclients;

import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.WordUtil;
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
		this.title = StringUtil.isEmpty(title) ? WordUtil.capitalizeFully(this.name().replace("_ERROR", "")) : title;
	}

}