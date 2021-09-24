package gg.sbs.api.mojang;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.sbs.api.scheduler.Scheduler;
import gg.sbs.api.http_old.*;
import gg.sbs.api.http_old.exceptions.HttpConnectionException;
import gg.sbs.api.mojang.exceptions.ProfileNotFoundException;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.ListUtil;
import gg.sbs.api.util.callback.ResultCallback;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;
import gg.sbs.api.util.concurrent.ConcurrentSet;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * A collection of methods to query UniqueIDs and Names.
 */
public abstract class MojangRepository<T extends MojangProfile> {

	static final transient Gson GSON = new Gson();
	private static final int PROFILES_PER_REQUEST = 10;
	private static final long WAIT_BETWEEN_REQUEST = 1000;
	private static long LAST_HTTP_REQUEST = System.currentTimeMillis();
	static boolean MOJANG_API_AVAILABLE = true;
	private final transient ConcurrentSet<T> cache = Concurrent.newSet();

	public MojangRepository() { }

	static {
		Scheduler.getInstance().runAsync(() -> {
			boolean available = false;

			try {
				HttpResponse response = HttpClient.get(Services.SERVICE_MOJANG_STATUS);
				JsonArray services = new JsonParser().parse(response.getBody().toString()).getAsJsonArray();

				for (int i = 0; i < services.size(); i++) {
					JsonObject status = services.get(i).getAsJsonObject();

					if (status.get(Services.SERVICE_MOJANG_API.getHost()) != null)
						available = !"red".equals(status.get(Services.SERVICE_MOJANG_API.getHost()).getAsString());
				}
			} catch (Exception ignore) { }

			MOJANG_API_AVAILABLE = available;
		}, 0, 6000);
	}

	@SuppressWarnings("unchecked")
	protected final Class<T> getSuperClass() {
		ParameterizedType superClass = (ParameterizedType)this.getClass().getGenericSuperclass();
		return (Class<T>)(superClass.getActualTypeArguments().length == 0 ? MojangProfile.class : superClass.getActualTypeArguments()[0]);
	}

	@SuppressWarnings("unchecked")
	protected final Class<T[]> getSuperClassArray() {
		return (Class<T[]>)Array.newInstance(this.getSuperClass(), 0).getClass();
	}

	/*
	 * Locates the profile associated with the given player.
	 *
	 * @param player Player to search with.
	 * @return Profile associated with the given player.
	 * @throws ProfileNotFoundException If unable to locate users profile.
	 */
	/*public final MojangProfile searchByPlayer(EntityPlayer player) throws ProfileNotFoundException {
		try {
			return this.searchByPlayer(Collections.singletonList(player))[0];
		} catch (ProfileNotFoundException pnfex) {
			if (ProfileNotFoundException.Reason.INVALID_PLAYER == pnfex.getReason()) {
				JsonObject json = new JsonObject();
				json.addProperty("id", player.getUniqueID().toString());
				json.addProperty("name", player.getName());
				throw new ProfileNotFoundException(ProfileNotFoundException.Reason.INVALID_PLAYER, ProfileNotFoundException.LookupType.OFFLINE_PLAYER, GSON.fromJson(json, MojangProfile.class));
			}

			throw pnfex;
		}
	}*/

	/**
	 * Locates the name history associated with the given Unique ID.
	 *
	 * @param uniqueId Unique ID to search with.
	 * @return Name history associated with the given Unique ID.
	 * @throws ProfileNotFoundException If unable to locate users profile.
	 */
	public final MojangProfileHistory[] getUniqueIdHistory(UUID uniqueId) {
		Preconditions.checkNotNull(uniqueId, "Unique ID cannot be NULL!");
		ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.UNIQUE_ID;
		HttpStatus status = HttpStatus.OK;

		try {
			if (MOJANG_API_AVAILABLE) {
				try {
					long wait = LAST_HTTP_REQUEST + WAIT_BETWEEN_REQUEST - System.currentTimeMillis();
					if (wait > 0) Thread.sleep(wait);
					HttpResponse response = HttpClient.get(Services.getNameHistoryUrl(uniqueId));

					if (HttpStatus.NO_CONTENT != response.getStatus()) {
						MojangProfileHistory[] results = GSON.fromJson(response.getBody().toString(), MojangProfileHistory[].class);

						if (results != null && results.length > 0)
							return results;
					}
				} catch (HttpConnectionException hcex) {
					if (HttpStatus.TOO_MANY_REQUESTS != (status = hcex.getStatus()))
						throw hcex;
				} finally {
					LAST_HTTP_REQUEST = System.currentTimeMillis();
				}
			} else
				throw new ProfileNotFoundException(ProfileNotFoundException.Reason.API_UNAVAILABLE, type, uniqueId);
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, uniqueId);
		}

		ProfileNotFoundException.Reason reason = ProfileNotFoundException.Reason.INVALID_PLAYER;

		if (status == HttpStatus.TOO_MANY_REQUESTS)
			reason = ProfileNotFoundException.Reason.RATE_LIMITED;

		throw new ProfileNotFoundException(reason, type, uniqueId);
	}

	/**
	 * Locates the name history associated with the given Unique ID.
	 *
	 * @param uniqueId Unique ID to search with.
	 * @param callback The callback to handle the result or error with.
	 */
	public final void getUniqueIdHistory(UUID uniqueId, ResultCallback<MojangProfileHistory[], ProfileNotFoundException> callback) {
		Preconditions.checkNotNull(callback, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			MojangProfileHistory[] profiles = null;
			ProfileNotFoundException error = null;

			try {
				profiles = this.getUniqueIdHistory(uniqueId);
			} catch (ProfileNotFoundException pnfex) {
				error = pnfex;
			}

			callback.handle(profiles, error);
		});
	}

	/*
	 * Locates the profile associated with the given player.
	 *
	 * @param player Player to search with.
	 * @param callback Callback to handle the result or error with.
	 */
	/*public final void searchByPlayer(EntityPlayer player, Callback<MojangProfile, ProfileNotFoundException> callback) {
		Preconditions.checkNotNull(callback, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			MojangProfile profile = null;
			ProfileNotFoundException error = null;

			try {
				profile = this.searchByPlayer(player);
			} catch (ProfileNotFoundException pnfex) {
				error = pnfex;
			}

			callback.handle(profile, error);
		});
	}*/

	/*
	 * Locates the profiles associated with the given players.
	 *
	 * @param players Players to search with.
	 * @return Profiles associated with the given players.
	 * @throws ProfileNotFoundException If unable to locate any users profile.
	 */
	/*public final MojangProfile[] searchByPlayer(EntityPlayer[] players) throws ProfileNotFoundException {
		return this.searchByPlayer(Arrays.asList(players));
	}*/

	/*
	 * Locates the profiles associated with the given players.
	 *
	 * @param players Players to search with.
	 * @param callback Callback to handle the result or error with.
	 */
	/*public final void searchByPlayer(EntityPlayer[] players, Callback<MojangProfile[], ProfileNotFoundException> callback) {
		Preconditions.checkNotNull(callback, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			MojangProfile[] profiles = null;
			ProfileNotFoundException error = null;

			try {
				profiles = this.searchByPlayer(players);
			} catch (ProfileNotFoundException pnfex) {
				error = pnfex;
			}

			callback.handle(profiles, error);
		});
	}*/

	/*
	 * Locates the profiles associated with the given players.
	 *
	 * @param players Players to search with.
	 * @return Profiles associated with the given players.
	 * @throws ProfileNotFoundException If unable to locate any users profile.
	 */
	/*public final MojangProfile[] searchByPlayer(Collection<? extends EntityPlayer> players) throws ProfileNotFoundException {
		Preconditions.checkArgument(ListUtil.notEmpty(players), "Players cannot be Empty!");
		final ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.OFFLINE_PLAYERS;
		ConcurrentList<MojangProfile> profiles = Concurrent.newList();
		ConcurrentList<MojangProfile> tempProfiles = Concurrent.newList();

		try {
			// Create Temporary Matching Profiles
			for (EntityPlayer player : players) {
				JsonObject json = new JsonObject();
				json.addProperty("id", player.getUniqueID().toString());
				json.addProperty("name", player.getName());
				tempProfiles.add(GSON.fromJson(json, MojangProfile.class));
			}

			// Search Unique ID
			for (MojangProfile temp : tempProfiles) {
				MojangProfile profile = this.searchByUniqueId(temp.getUniqueId());
				profiles.add(profile);
				tempProfiles.remove(temp);
			}

			return ListUtil.toArray(profiles, MojangProfile.class);
		} catch (ProfileNotFoundException pnfex) {
			throw new ProfileNotFoundException(pnfex.getReason(), type, pnfex.getCause(), ListUtil.toArray(tempProfiles, MojangProfile.class));
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, ListUtil.toArray(tempProfiles, MojangProfile.class));
		}
	}*/

	/*
	 * Locates the profiles associated with the given players.
	 *
	 * @param players Players to search with.
	 * @param callback Callback to handle the result or error with.
	 */
	/*public final void searchByPlayer(Collection<EntityPlayer> players, Callback<MojangProfile[], ProfileNotFoundException> callback) {
		Preconditions.checkArgument(callback != null, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			MojangProfile[] profiles = null;
			ProfileNotFoundException error = null;

			try {
				profiles = this.searchByPlayer(players);
			} catch (ProfileNotFoundException pnfex) {
				error = pnfex;
			}

			callback.handle(profiles, error);
		});
	}*/

	/**
	 * Locates the profile associated with the given username.
	 *
	 * @param username Username to search with.
	 * @return Profile associated with the given username.
	 * @throws ProfileNotFoundException If unable to locate users profile.
	 */
	public final T searchByUsername(String username) throws ProfileNotFoundException {
		try {
			return this.searchByUsername(Collections.singletonList(username))[0];
		} catch (ProfileNotFoundException pnfex) {
			throw new ProfileNotFoundException(pnfex.getReason(), ProfileNotFoundException.LookupType.USERNAME, pnfex.getCause(), username);
		}
	}

	/**
	 * Locates the profile associated with the given username.
	 *
	 * @param username Username to search with.
	 * @param callback Callback to handle the result or error with.
	 */
	public final void searchByUsername(String username, ResultCallback<T, ProfileNotFoundException> callback) {
		Preconditions.checkArgument(callback != null, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			T profile = null;
			ProfileNotFoundException error = null;

			try {
				profile = this.searchByUsername(username);
			} catch (ProfileNotFoundException pnfex) {
				error = pnfex;
			}

			callback.handle(profile, error);
		});
	}

	/**
	 * Locates the profiles associated with the given usernames.
	 *
	 * @param usernames Usernames to search with.
	 * @return Profiles associated with the given usernames.
	 * @throws ProfileNotFoundException If unable to locate any users profile.
	 */
	public final T[] searchByUsername(String[] usernames) throws ProfileNotFoundException {
		return this.searchByUsername(Arrays.asList(usernames));
	}

	/**
	 * Locates the profiles associated with the given usernames.
	 *
	 * @param usernames Usernames to search with.
	 * @param callback Callback to handle the result or error with.
	 */
	public final void searchByUsername(String[] usernames, ResultCallback<T[], ProfileNotFoundException> callback) {
		Preconditions.checkArgument(callback != null, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			T[] profiles = null;
			ProfileNotFoundException error = null;

			try {
				profiles = this.searchByUsername(usernames);
			} catch (ProfileNotFoundException pnfex) {
				error = pnfex;
			}

			callback.handle(profiles, error);
		});
	}

	/**
	 * Locates the profiles associated with the given usernames.
	 *
	 * @param usernames Usernames to search with.
	 * @return Profiles associated with the given usernames.
	 * @throws ProfileNotFoundException If unable to locate any users profile.
	 */
	public final T[] searchByUsername(Collection<String> usernames) throws ProfileNotFoundException {
		Preconditions.checkArgument(ListUtil.notEmpty(usernames), "Usernames cannot be Empty!");
		final ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.USERNAMES;
		ConcurrentList<T> profiles = Concurrent.newList();
		HttpStatus status = HttpStatus.OK;

		try {
			ConcurrentList<String> userList = Concurrent.newList(usernames);

			// Remove Expired Cache Profiles
			this.cache.removeIf(MojangProfile::hasExpired);

			// Check Cache Profiles
			if (!this.cache.isEmpty()) {
				for (String name : userList) {
					String criteriaName = name.toLowerCase();

					for (T profile : this.cache) {
						if (profile.getName().equalsIgnoreCase(criteriaName)) {
							profiles.add(profile);
							userList.remove(name);
							break;
						}
					}
				}
			}

			// Check Online Players
			this.processOnlineUsernames(profiles, userList);
			/*for (String name : userList) {
				String criteriaName = name.toLowerCase();

				for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
					if (player.getName().equalsIgnoreCase(criteriaName)) {
						JsonObject json = new JsonObject();
						json.addProperty("id", player.getUniqueID().toString());
						json.addProperty("name", player.getName());
						profiles.add(GSON.fromJson(json.toString(), MojangProfile.class));
					}
				}
			}*/

			// Query Mojang API
			if (!userList.isEmpty()) {
				if (MOJANG_API_AVAILABLE) {
					HttpHeader contentType = new HttpHeader("Content-Type", "application/json");
					String[] userArray = ListUtil.toArray(userList, String.class);
					int start = 0;
					int i = 0;

					do {
						int end = PROFILES_PER_REQUEST * (i + 1);
						if (end > userList.size()) end = userList.size();
						String[] batch = Arrays.copyOfRange(userArray, start, end);
						HttpBody body = new HttpBody(GSON.toJson(batch));
						long wait = LAST_HTTP_REQUEST + WAIT_BETWEEN_REQUEST - System.currentTimeMillis();

						try {
							if (wait > 0) Scheduler.sleep(wait);
							HttpResponse response = HttpClient.post(Services.API_NAME_TO_UUID, body, contentType);

							if (HttpStatus.NO_CONTENT != response.getStatus()) {
								T[] result = GSON.fromJson(response.getBody().toString(), this.getSuperClassArray());

								if (result != null && result.length > 0) {
									profiles.addAll(Arrays.asList(result));
									this.cache.addAll(Arrays.asList(result));
								}
							}
						} catch (HttpConnectionException hcex) {
							if (HttpStatus.TOO_MANY_REQUESTS == (status = hcex.getStatus()))
								break;

							throw hcex;
						} finally {
							LAST_HTTP_REQUEST = System.currentTimeMillis();
							start = end;
							i++;
						}
					} while (start < userList.size());

					for (T profile : profiles)
						userList.remove(profile.getName());

					for (String user : userList) {
						long wait = LAST_HTTP_REQUEST + WAIT_BETWEEN_REQUEST - System.currentTimeMillis();

						try {
							if (wait > 0) Thread.sleep(wait);
							HttpResponse response = HttpClient.get(Services.getNameUrl(user));

							if (HttpStatus.NO_CONTENT != response.getStatus()) {
								T result = GSON.fromJson(response.getBody().toString(), this.getSuperClass());

								if (result != null) {
									profiles.add(result);
									this.cache.add(result);
								}
							}
						} catch (HttpConnectionException hcex) {
							if (HttpStatus.TOO_MANY_REQUESTS == (status = hcex.getStatus()))
								break;

							throw hcex;
						} finally {
							LAST_HTTP_REQUEST = System.currentTimeMillis();
						}
					}

					for (T profile : profiles)
						userList.remove(profile.getName());

					for (String user : userList) {
						long wait = LAST_HTTP_REQUEST + WAIT_BETWEEN_REQUEST - System.currentTimeMillis();

						try {
							if (wait > 0) Thread.sleep(wait);
							HttpResponse response = HttpClient.get(Services.getNameUrl(user, false));

							if (HttpStatus.NO_CONTENT != response.getStatus()) {
								T result = GSON.fromJson(response.getBody().toString(), this.getSuperClass());

								if (result != null) {
									profiles.add(result);
									this.cache.add(result);
								}
							}
						} catch (HttpConnectionException hcex) {
							if (HttpStatus.TOO_MANY_REQUESTS == (status = hcex.getStatus()))
								break;

							throw hcex;
						} finally {
							LAST_HTTP_REQUEST = System.currentTimeMillis();
						}
					}
				} else
					throw new ProfileNotFoundException(ProfileNotFoundException.Reason.API_UNAVAILABLE, type, ListUtil.toArray(usernames, String.class));
			}
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, ListUtil.toArray(usernames, String.class));
		}

		if (profiles.isEmpty()) {
			ProfileNotFoundException.Reason reason = ProfileNotFoundException.Reason.INVALID_PLAYER;

			if (status == HttpStatus.TOO_MANY_REQUESTS)
				reason = ProfileNotFoundException.Reason.RATE_LIMITED;

			throw new ProfileNotFoundException(reason, type, ListUtil.toArray(usernames, String.class));
		}

		return ListUtil.toArray(profiles, this.getSuperClass());
	}

	/**
	 * Locates the profiles associated with the given usernames.
	 *
	 * @param usernames Usernames to search with.
	 * @param callback Callback to handle the result or error with.
	 */
	public final void searchByUsername(Collection<String> usernames, ResultCallback<T[], ProfileNotFoundException> callback) {
		Preconditions.checkArgument(callback != null, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			T[] profiles = null;
			ProfileNotFoundException error = null;

			try {
				profiles = this.searchByUsername(usernames);
			} catch (ProfileNotFoundException pnfex) {
				error = pnfex;
			}

			callback.handle(profiles, error);
		});
	}

	protected abstract T processOnlineUniqueId(UUID uniqueId);

	protected abstract void processOnlineUsernames(List<T> profiles, ConcurrentList<String> userList);

	/**
	 * Locates the profile associated with the given Unique ID.
	 *
	 * @param uniqueId Unique ID to search with.
	 * @return Profile associated with the given Unique ID.
	 * @throws ProfileNotFoundException If unable to locate users profile.
	 */
	public final T searchByUniqueId(UUID uniqueId) throws ProfileNotFoundException {
		Preconditions.checkNotNull(uniqueId, "Unique ID cannot be NULL!");
		ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.UNIQUE_ID;
		T found = null;
		HttpStatus status = HttpStatus.OK;

		try {
			// Remove Expired Cache Profiles
			this.cache.removeIf(MojangProfile::hasExpired);

			// Check Cache Profiles
			for (T profile : this.cache) {
				if (profile.getUniqueId().equals(uniqueId)) {
					found = profile;
					break;
				}
			}

			// Check Online Players
			if (found == null)
				found = this.processOnlineUniqueId(uniqueId);
			/*for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
				if (player.getUniqueID().equals(uniqueId)) {
					JsonObject json = new JsonObject();
					json.addProperty("id", uniqueId.toString());
					json.addProperty("name", player.getName());
					found = GSON.fromJson(json.toString(), MojangProfile.class);
				}
			}*/

			if (found == null) {
				MojangProfileHistory[] historyProfiles = this.getUniqueIdHistory(uniqueId);
				MojangProfileHistory result = historyProfiles[historyProfiles.length - 1];
				JsonObject json = new JsonObject();
				json.addProperty("id", uniqueId.toString());
				json.addProperty("name", result.name);
				found = GSON.fromJson(json.toString(), this.getSuperClass());
				this.cache.add(found);
			}
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, uniqueId);
		}

		if (found == null) {
			ProfileNotFoundException.Reason reason = ProfileNotFoundException.Reason.INVALID_PLAYER;

			if (status == HttpStatus.TOO_MANY_REQUESTS) // TODO: Fix
				reason = ProfileNotFoundException.Reason.RATE_LIMITED;

			throw new ProfileNotFoundException(reason, type, uniqueId);
		}

		return found;
	}

	/**
	 * Locates the profile associated with the given Unique ID.
	 *
	 * @param uniqueId Unique ID to search with.
	 * @param callback The callback to handle the result or error with.
	 */
	public final void searchByUniqueId(UUID uniqueId, ResultCallback<MojangProfile, ProfileNotFoundException> callback) {
		Preconditions.checkArgument(callback != null, "Callback cannot be NULL!");

		Scheduler.getInstance().runAsync(() -> {
			MojangProfile profile = null;
			ProfileNotFoundException error = null;

			try {
				profile = this.searchByUniqueId(uniqueId);
			} catch (ProfileNotFoundException pnfex) {
				error = pnfex;
			}

			callback.handle(profile, error);
		});
	}

	/**
	 * Mojang Service and API URLs
	 */
	@SuppressWarnings("unused")
	public static final class Services {

		// API: http://wiki.vg/Mojang_API
		// https://visage.surgeplay.com/index.html
		public static final URL SERVICE_MINECRAFT = getUrl("minecraft.net");
		public static final URL SERVICE_MOJANG_ACCOUNT = getUrl("account.mojang.com");
		public static final URL SERVICE_MOJANG_API = getUrl("api.mojang.com");
		public static final URL SERVICE_MOJANG_AUTH = getUrl("auth.mojang.com");
		public static final URL SERVICE_MOJANG_AUTHSERVER = getUrl("authserver.mojang.com");
		public static final URL SERVICE_MOJANG_SESSION = getUrl("sessionserver.mojang.com");
		public static final URL SERVICE_MOJANG_STATUS = getUrl("status.mojang.com/check");
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
				throw new IllegalArgumentException(FormatUtil.format("Unable to create URL {0}!", host));
			}
		}

	}

}