package gg.sbs.api.hypixel;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import gg.sbs.api.http.HttpClient;
import gg.sbs.api.http.HttpParameter;
import gg.sbs.api.http.HttpResponse;
import gg.sbs.api.http.HttpStatus;
import gg.sbs.api.http.exceptions.HttpConnectionException;
import gg.sbs.api.mojang.MojangProfile;
import gg.sbs.api.scheduler.Scheduler;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.callback.Callback;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import gg.sbs.api.util.concurrent.ConcurrentSet;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HypixelAPI {

	private static final transient Gson GSON = new GsonBuilder()
			.registerTypeAdapter(HypixelProfile.Stats.SkyblockStats.class, new HypixelProfile.Stats.Deserializer())
			.registerTypeAdapter(SkyblockIsland.class, new SkyblockIsland.Deserializer())
			.setPrettyPrinting().create();

	// TODO: linfoot@hypixel.net - Connor - special gg.sbs.api key
	// nahydrin - 38ce2e599db9443aa9a57bd748153fcc
	// craftedfury - f33f51a796914076abdaf66e3d047a71
	private final int maxRquestsPerMinute = 120;
	private final Key apiKey;
	private final transient ConcurrentSet<HypixelProfile> cache = Concurrent.newSet();
	private final ConcurrentList<Long> previousRequests = Concurrent.newList();

	public HypixelAPI(Key apiKey) {
		this.apiKey = apiKey;
		Scheduler.getInstance().schedule(() -> this.previousRequests.removeIf(request -> System.currentTimeMillis() >= (request + 60000)), 0, 20);
	}

	public final Key getApiKey() {
		return this.apiKey;
	}

	public final long getOldestHttpRequest() {
		return this.previousRequests.get(0);
	}

	public final long getLastHttpRequest() {
		return this.previousRequests.get(this.previousRequests.size() - 1);
	}

	public final HypixelGuild getHypixelGuild(MojangProfile profile) throws HypixelApiException {
		Preconditions.checkArgument(profile != null, "Profile cannot be NULL!");
		JsonObject guildQuery = this.query(Endpoint.GUILD, "player", profile.getUniqueId().toString().replaceAll("-", "")).getAsJsonObject();
		return GSON.fromJson(guildQuery, HypixelGuild.class);
	}

	public final void getHypixelGuild(MojangProfile profile, Callback<HypixelGuild, HypixelApiException> callback) {
		Preconditions.checkArgument(callback != null, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			HypixelGuild hypixelGuild = null;
			HypixelApiException error = null;

			try {
				hypixelGuild = this.getHypixelGuild(profile);
			} catch (HypixelApiException haex) {
				error = haex;
			}

			callback.handle(hypixelGuild, error);
		});
	}

	public final HypixelProfile getHypixelProfile(MojangProfile profile) throws HypixelApiException {
		Preconditions.checkArgument(profile != null, "Profile cannot be NULL!");
		HypixelProfile hypixelProfile = null;

		// Remove Expired Cache Profiles
		this.cache.removeIf(HypixelProfile::hasExpired);

		// Check Cache Profiles
		if (!this.cache.isEmpty()) {
			for (HypixelProfile cachedProfile : this.cache) {
				if (cachedProfile.getUniqueId().equals(profile.getUniqueId())) {
					hypixelProfile = cachedProfile;
					break;
				}
			}
		}

		if (hypixelProfile == null) {
			JsonObject playerQuery = this.query(Endpoint.PLAYER, "uuid", profile.getUniqueId().toString().replaceAll("-", "")).getAsJsonObject();
			hypixelProfile = GSON.fromJson(playerQuery, HypixelProfile.class);

			try {
				hypixelProfile.hypixelGuild = this.getHypixelGuild(profile);
			} catch (HypixelApiException ignored) { }
			//PlayerUtil.setClipboard(GSON.toJson(playerQuery)); // TODO
		}

		JsonObject statusQuery = this.query(Endpoint.STATUS, "uuid", profile.getUniqueId().toString().replaceAll("-", "")).getAsJsonObject();
		hypixelProfile.session = GSON.fromJson(statusQuery, HypixelProfile.Session.class);
		this.cache.add(hypixelProfile);
		return hypixelProfile;
	}

	public final void getHypixelProfile(MojangProfile profile, Callback<HypixelProfile, HypixelApiException> callback) {
		Preconditions.checkArgument(callback != null, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			HypixelProfile hypixelProfile = null;
			HypixelApiException error = null;

			try {
				hypixelProfile = this.getHypixelProfile(profile);
			} catch (HypixelApiException haex) {
				error = haex;
			}

			callback.handle(hypixelProfile, error);
		});
	}

	@SuppressWarnings("unchecked")
	public final ConcurrentMap<String, Object> getSession(HypixelProfile hypixelProfile) throws HypixelApiException {
		Preconditions.checkArgument(hypixelProfile != null, "Hypixel profile cannot be NULL!");
		JsonObject statusQuery = this.query(Endpoint.STATUS, "uuid", hypixelProfile.getUniqueId().toString().replaceAll("-", "")).getAsJsonObject();
		return (ConcurrentMap<String, Object>)GSON.fromJson(statusQuery, ConcurrentMap.class);
	}

	public final SkyblockIsland getSkyblockIsland(HypixelProfile hypixelProfile) throws HypixelApiException {
		return this.getSkyblockIsland(hypixelProfile, hypixelProfile.getFirstSkyblockProfile());
	}

	public final SkyblockIsland getSkyblockIsland(HypixelProfile hypixelProfile, HypixelProfile.SkyblockProfile skyblockProfile) throws HypixelApiException {
		Preconditions.checkArgument(hypixelProfile != null, "Hypixel profile cannot be NULL!");
		Preconditions.checkArgument(skyblockProfile != null, "Skyblock profile UUID cannot be NULL!");
		JsonObject profileQuery = this.query(Endpoint.PROFILE, "profile", skyblockProfile.getUniqueId().toString().replaceAll("-", "")).getAsJsonObject();

		// TODO: DEBUG MODE
		//if (Cache.DEBUG_MODE)
		//	PlayerUtil.setClipboard(GSON.toJson(profileQuery));

		SkyblockIsland skyblockIsland = GSON.fromJson(profileQuery, SkyblockIsland.class);
		skyblockIsland.cute_name = skyblockProfile.getCuteName();
		skyblockIsland.setCurrentMember(hypixelProfile.getUniqueId());
		return skyblockIsland;
	}

	public final ConcurrentSet<SkyblockIsland> getSkyblockIslands(HypixelProfile hypixelProfile) throws HypixelApiException {
		Preconditions.checkArgument(hypixelProfile != null, "Hypixel profile cannot be NULL!");
		JsonArray profilesQuery = this.query(Endpoint.PROFILES, "uuid", hypixelProfile.getUniqueId().toString().replaceAll("-", "")).getAsJsonArray();
		ConcurrentSet<SkyblockIsland> skyblockIslands = Concurrent.newSet();

		for (int i = 0; i < profilesQuery.size(); i++) {
			SkyblockIsland skyblockIsland = GSON.fromJson(profilesQuery.get(i).getAsJsonObject(), SkyblockIsland.class);
			skyblockIsland.setCurrentMember(hypixelProfile.getUniqueId());
			skyblockIslands.add(skyblockIsland);
		}

		return skyblockIslands;
	}

	public final void getSkyblockIsland(HypixelProfile hypixelProfile, Callback<SkyblockIsland, HypixelApiException> callback) {
		this.getSkyblockIsland(hypixelProfile, hypixelProfile.getFirstSkyblockProfile(), callback);
	}

	public final void getSkyblockIsland(HypixelProfile hypixelProfile, HypixelProfile.SkyblockProfile skyblockProfile, Callback<SkyblockIsland, HypixelApiException> callback) throws HypixelApiException {
		Preconditions.checkArgument(callback != null, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			SkyblockIsland skyblockIsland = null;
			HypixelApiException error = null;

			try {
				skyblockIsland = this.getSkyblockIsland(hypixelProfile, skyblockProfile);
			} catch (HypixelApiException haex) {
				error = haex;
			}

			callback.handle(skyblockIsland, error);
		});
	}

	public final JsonElement query(Endpoint endpoint, String name, String value) throws HypixelApiException {
		return this.query(endpoint, Collections.singletonList(new HttpParameter(name, value)));
	}

	public final JsonElement query(Endpoint endpoint, List<HttpParameter> parameters) throws HypixelApiException {
		long wait = 0;

		if (this.previousRequests.size() >= this.maxRquestsPerMinute)
			wait = this.getOldestHttpRequest() + 60000 - System.currentTimeMillis();

		Scheduler.sleep(wait);

		try {
			URIBuilder builder = new URIBuilder(endpoint.getUrl().toURI());
			builder.addParameter("key", this.getApiKey().getValue());

			for (HttpParameter parameter : parameters) {
				if (!"key".equalsIgnoreCase(parameter.getName()))
					builder.addParameter(parameter.getName(), parameter.getValue());
			}

			URL url = builder.build().toURL();
			HttpResponse response = HttpClient.get(url);

			if (HttpStatus.NO_CONTENT != response.getStatus()) {
				JsonObject body = new JsonParser().parse(response.getBody().toString()).getAsJsonObject();

				if (body.get("success").getAsBoolean()) {
					if (body.has(endpoint.getFocus())) {
						if (!body.get(endpoint.getFocus()).isJsonNull())
							return body.get(endpoint.getFocus());
						else {
							String query = parameters.stream().map(parameter -> parameter.getName() + "=" + parameter.getValue()).collect(Collectors.joining("&"));
							throw new HypixelApiException(HypixelApiException.Reason.API_EXCEPTION, endpoint, StringUtil.format("The provided parameters ({0}) are invalid.", query));
						}
					} else
						throw new HypixelApiException(HypixelApiException.Reason.API_EXCEPTION, endpoint, StringUtil.format("The built-in endpoint focus ({0}) is invalid.", endpoint.getFocus())); // Very bad
				} else {
					String cause = body.get("cause").getAsString();

					if ("Invalid API key".equalsIgnoreCase(cause))
						throw new HypixelApiException(HypixelApiException.Reason.INVALID_API_KEY, endpoint, "The provided API key is invalid.");
					else
						throw new HypixelApiException(HypixelApiException.Reason.API_EXCEPTION, endpoint, cause); // This will never throw... unless Hypixel changes the API
				}
			} else
				throw new HypixelApiException(HypixelApiException.Reason.API_EXCEPTION, endpoint, response.getStatus().getMessage());
		} catch (URISyntaxException | MalformedURLException ex) {
			throw new HypixelApiException(HypixelApiException.Reason.EXCEPTION, endpoint, ex.getMessage(), ex);
		} catch (HttpConnectionException hcex) {
			String message = StringUtil.format("Error {0}: {1}", hcex.getStatus().getCode(), hcex.getMessage());

			if (hcex.getStatus() == HttpStatus.BAD_REQUEST) { // User Error
				JsonObject body = new JsonParser().parse(hcex.getBody().toString()).getAsJsonObject();
				message = body.get("cause").getAsString();
			}

			throw new HypixelApiException(HypixelApiException.Reason.API_EXCEPTION, endpoint, message, hcex);
		} finally {
			this.previousRequests.add(System.currentTimeMillis());
		}
	}

	public enum Endpoint {

		PLAYER("player"),
		SESSION("session"),
		FRIENDS("friends", "records"),
		BOOSTERS("boosters"),
		LEADERBOARDS("leaderboards"),
		NEWS("skyblock/news", "items"),
		PROFILE("skyblock/profile", "profile"),
		PROFILES("skyblock/profiles", "profiles"),
		AUCTION("skyblock/auction", "auctions"),
		AUCTIONS("skyblock/auctions", "auctions"),
		BANK("skyblock/bank", "bank"),
		PLAYER_COUNT("playerCount"),
		WATCHDOG_STATS("watchdogstats", null),
		FIND_GUILD("findGuild", "guild"),
		STATUS("status", "session"),
		GUILD("guild");

		private final String endpoint;
		private final String focus;

		Endpoint(String endpoint) {
			this(endpoint, endpoint);
		}

		Endpoint(String endpoint, String focus) {
			this.endpoint = endpoint;
			this.focus = focus;
		}

		public String getEndpoint() {
			return this.endpoint;
		}

		public String getFocus() {
			return this.focus;
		}

		public URL getUrl() {
			return Services.getUrl(StringUtil.format("{0}/{1}", Services.SERVICE_HYPIXEL_API.toString(), this.getEndpoint()));
		}

	}

	public static class Key {

		public static final Pattern REGEX = Pattern.compile("[a-z0-9]{8}-(?:[a-z0-9]{4}-){3}[a-z0-9]{12}");
		private final String value;

		public Key(String value) {
			Preconditions.checkNotNull(value, "Hypixel API Key cannot be NULL!");
			Preconditions.checkArgument(REGEX.matcher(value).matches(), "Hypixel API Key is INVALID!");
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

	/**
	 * Mojang Service and API URLs
	 */
	@SuppressWarnings("unused")
	public static final class Services {

		// API: http://wiki.vg/Mojang_API
		// https://visage.surgeplay.com/index.html
		public static final URL SERVICE_HYPIXEL = getUrl("hypixel.net");
		public static final URL SERVICE_HYPIXEL_API = getUrl("gg.sbs.api.hypixel.net");

		public static URL getApiUrl(Endpoint endpoint) {
			return getUrl(StringUtil.format("{0}/{1}", SERVICE_HYPIXEL_API.toString(), endpoint.getEndpoint()));
		}

		public static URL getUrl(String host) {
			if (!host.startsWith("https://") && !host.startsWith("http://"))
				host = StringUtil.format("https://{0}", host);

			try {
				return new URL(host);
			} catch (MalformedURLException muex) {
				throw new IllegalArgumentException(StringUtil.format("Unable to create URL {0}!", host));
			}
		}

	}

}