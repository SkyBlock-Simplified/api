package gg.sbs.api.apiclients.mojang;

import gg.sbs.api.util.helper.FormatUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Mojang Service and API URLs
 */
public final class MojangServices {

	// API: http://wiki.vg/Mojang_API
	// https://visage.surgeplay.com/index.html
	public static final URL SERVICE_MOJANG = getUrl("mojang.com");
	public static final URL SERVICE_MOJANG_ACCOUNT = getUrl("account.mojang.com");
	public static final URL SERVICE_MOJANG_API = getUrl("api.mojang.com");
	public static final URL SERVICE_MOJANG_AUTH = getUrl("auth.mojang.com");
	public static final URL SERVICE_MOJANG_AUTHSERVER = getUrl("authserver.mojang.com");
	public static final URL SERVICE_MOJANG_SESSION = getUrl("sessionserver.mojang.com");
	public static final URL SERVICE_MOJANG_STATUS = getUrl("status.mojang.com/check");
	public static final URL SERVICE_MINECRAFT = getUrl("minecraft.net");
	public static final URL SERVICE_MINECRAFT_SESSION = getUrl("session.minecraft.net");
	public static final URL SERVICE_MINECRAFT_SKINS = getUrl("skins.minecraft.net");
	public static final URL SERVICE_MINECRAFT_TEXTURES = getUrl("textures.minecraft.net");

	public static final URL API_NAME_TO_UUID = getUrl(FormatUtil.format("{0}/profiles/minecraft", SERVICE_MOJANG_API.getHost()));
	public static final URL API_NAME_TO_UUID_AT = getUrl(FormatUtil.format("{0}/users/profiles/minecraft", SERVICE_MOJANG_API.getHost()));
	public static final URL SESSIONSERVER_SKIN_CAPE = getUrl(FormatUtil.format("{0}/session/minecraft/profile", SERVICE_MOJANG_SESSION.getHost()));
	public static final URL AUTHSERVER_AUTHENTICATE = getUrl(FormatUtil.format("{0}/authenticate", SERVICE_MOJANG_AUTHSERVER.getHost()));
	public static final URL AUTHSERVER_REFRESH = getUrl(FormatUtil.format("{0}/refresh", SERVICE_MOJANG_AUTHSERVER.getHost()));
	public static final URL AUTHSERVER_VALIDATE = getUrl(FormatUtil.format("{0}/validate", SERVICE_MOJANG_AUTHSERVER.getHost()));
	public static final URL AUTHSERVER_SIGNOUT = getUrl(FormatUtil.format("{0}/signout", SERVICE_MOJANG_AUTHSERVER.getHost()));
	public static final URL AUTHSERVER_INVALIDATE = getUrl(FormatUtil.format("{0}/invalidate", SERVICE_MOJANG_AUTHSERVER.getHost()));

	public static URL getNameUrl(String username) {
		return getNameUrl(username, true);
	}

	public static URL getNameUrl(String username, boolean useAt) {
		return getUrl(FormatUtil.format("{0}/{1}{2}", API_NAME_TO_UUID_AT.toString(), username, (useAt ? "?at=0" : "")));
	}

	public static URL getNameHistoryUrl(UUID uniqueId) {
		return getUrl(FormatUtil.format("{0}/user/profiles/{1}/names", SERVICE_MOJANG_API.toString(), uniqueId.toString().replace("-", "")));
	}

	public static URL getPropertiesUrl(UUID uniqueId) {
		return getPropertiesUrl(uniqueId, true);
	}

	public static URL getPropertiesUrl(UUID uniqueId, boolean unsigned) {
		return getUrl(FormatUtil.format("{0}/{1}?unsigned={2}", SESSIONSERVER_SKIN_CAPE.toString(), uniqueId.toString().replace("-", ""), String.valueOf(unsigned)));
	}

	public static URL getUrl(String host) {
		if (!host.startsWith("https://") && !host.startsWith("http://"))
			host = FormatUtil.format("https://{0}", host);

		try {
			return new URL(host);
		} catch (MalformedURLException muex) {
			throw new IllegalArgumentException(FormatUtil.format("Unable to create URL ''{0}''", host));
		}
	}

}