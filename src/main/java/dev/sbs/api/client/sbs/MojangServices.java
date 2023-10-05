package dev.sbs.api.client.sbs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Mojang Service and API URLs
 */
public final class MojangServices {

    // API: http://wiki.vg/Mojang_API

    // Skin Renderer
    // https://visage.surgeplay.com/index.html
    // https://github.com/unascribed-archive/Visage
    public static final URL SERVICE_MOJANG = getUrl("mojang.com");
    public static final URL SERVICE_MOJANG_ACCOUNT = getUrl("account.mojang.com");
    public static final URL SERVICE_MOJANG_API = getUrl("api.mojang.com");
    public static final URL SERVICE_MOJANG_AUTH = getUrl("auth.mojang.com");
    public static final URL SERVICE_MOJANG_AUTHSERVER = getUrl("authserver.mojang.com");
    public static final URL SERVICE_MOJANG_SESSION = getUrl("sessionserver.mojang.com");
    public static final URL SERVICE_MINECRAFT = getUrl("minecraft.net");
    public static final URL SERVICE_MINECRAFT_SESSION = getUrl("session.minecraft.net");
    public static final URL SERVICE_MINECRAFT_SKINS = getUrl("skins.minecraft.net");
    public static final URL SERVICE_MINECRAFT_TEXTURES = getUrl("textures.minecraft.net");

    public static final URL API_NAME_TO_UUID = getUrl(String.format("%s/profiles/minecraft", SERVICE_MOJANG_API.getHost()));
    public static final URL API_NAME_TO_UUID_AT = getUrl(String.format("%s/users/profiles/minecraft", SERVICE_MOJANG_API.getHost()));
    public static final URL SESSIONSERVER_SKIN_CAPE = getUrl(String.format("%s/session/minecraft/profile", SERVICE_MOJANG_SESSION.getHost()));
    public static final URL AUTHSERVER_AUTHENTICATE = getUrl(String.format("%s/authenticate", SERVICE_MOJANG_AUTHSERVER.getHost()));
    public static final URL AUTHSERVER_REFRESH = getUrl(String.format("%s/refresh", SERVICE_MOJANG_AUTHSERVER.getHost()));
    public static final URL AUTHSERVER_VALIDATE = getUrl(String.format("%s/validate", SERVICE_MOJANG_AUTHSERVER.getHost()));
    public static final URL AUTHSERVER_SIGNOUT = getUrl(String.format("%s/signout", SERVICE_MOJANG_AUTHSERVER.getHost()));
    public static final URL AUTHSERVER_INVALIDATE = getUrl(String.format("%s/invalidate", SERVICE_MOJANG_AUTHSERVER.getHost()));

    public static URL getNameUrl(String username) {
        return getNameUrl(username, true);
    }

    public static URL getNameUrl(String username, boolean useAt) {
        return getUrl(String.format("%s/%s%s", API_NAME_TO_UUID_AT, username, (useAt ? "?at=0" : "")));
    }

    public static URL getNameHistoryUrl(UUID uniqueId) {
        return getUrl(String.format("%s/user/profiles/%s/names", SERVICE_MOJANG_API, uniqueId.toString().replace("-", "")));
    }

    public static URL getPropertiesUrl(UUID uniqueId) {
        return getPropertiesUrl(uniqueId, true);
    }

    public static URL getPropertiesUrl(UUID uniqueId, boolean unsigned) {
        return getUrl(String.format("%s/%s?unsigned=%s", SESSIONSERVER_SKIN_CAPE, uniqueId.toString().replace("-", ""), unsigned));
    }

    public static URL getUrl(String host) {
        if (!host.startsWith("https://") && !host.startsWith("http://"))
            host = String.format("https://%s", host);

        try {
            return new URL(host);
        } catch (MalformedURLException muex) {
            throw new IllegalArgumentException(String.format("Unable to create URL '%s'", host));
        }
    }

}
