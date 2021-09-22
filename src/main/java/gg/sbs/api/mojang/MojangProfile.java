package gg.sbs.api.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.sbs.api.scheduler.Scheduler;
import gg.sbs.api.util.DataUtil;
import gg.sbs.api.http.HttpClient;
import gg.sbs.api.http.HttpResponse;
import gg.sbs.api.http.HttpStatus;
import gg.sbs.api.http.exceptions.HttpConnectionException;
import gg.sbs.api.mojang.exceptions.ProfileNotFoundException;
import gg.sbs.api.util.callback.Callback;
import gg.sbs.api.util.StringUtil;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static gg.sbs.api.mojang.MojangRepository.MOJANG_API_AVAILABLE;

/**
 * Container for a players unique id and name.
 */
public class MojangProfile extends MojangProfileHistory {

	protected static final transient Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	protected static final long WAIT_BETWEEN_REQUEST = 60000;
	protected String id = "";
	protected UUID uuid;
	protected boolean legacy = false;
	protected boolean demo = false;
	protected URL skinUrl;
	protected URL capeUrl;
	protected long updated = System.currentTimeMillis();
	protected long lastHttpRequest = 0;

	private MojangProfile() { }

	/**
	 * Gets if the profile can currently send a request to the Mojang API
	 * to reload it's skin properties.
	 *
	 * @return True if it can send a request.
	 */
	public boolean canReloadProperties() {
		return this.lastHttpRequest + WAIT_BETWEEN_REQUEST < System.currentTimeMillis();
	}

	@Override
	public final boolean equals(Object obj) {
		return obj == this || obj instanceof MojangProfile && this.getUniqueId().equals(((MojangProfile)obj).getUniqueId());
	}

	/**
	 * Gets the base64 encoded skin texture associated with this profile.
	 *
	 * @return Encoded skin texture, null if profile has no skin or has not yet been requested.
	 * @see #loadProperties(Callback)
	 */
	public final String getSkinBase64() {
		if (this.skinUrl == null)
			return null;

		return new String(DataUtil.encode(StringUtil.format("'{'\"textures\":'{'\"SKIN\":'{'\"url\":\"{0}\"'}'}'}'", this.getSkinUrl().toString()).getBytes()));
	}

	/**
	 * Gets the cape associated with this profile.
	 *
	 * @return Cape url, null if profile has no cape or has not yet been requested.
	 * @see #loadProperties(Callback)
	 */
	public final URL getCapeUrl() {
		return this.capeUrl;
	}

	/**
	 * Gets the skin associated with this profile.
	 *
	 * @return Skin url, null if profile has no skin or has not yet been requested.
	 * @see #loadProperties(Callback)
	 */
	public final URL getSkinUrl() {
		return this.skinUrl;
	}

	// TODO: Migrate to mod version, ClientMojangProfile?
	/*
	 * Gets a skull skinned to this profile's face.
	 *
	 * @return Skull item with this profiles skin face.
	 */
	/*public final ItemStack getSkull() {
		return MinecraftAPI.getMiniBlockDatabase().create(this.getUniqueId(), StringUtil.preformat("{0}''s Head", ChatFormatting.RESET.toString(), this.getName()));
	}*/

	/*
	 * Gets a skull skinned to this profile's face.
	 * <br><br>
	 * Uses legacy method by having the game load the texture. This can result in steve heads.
	 *
	 * @return Skull item with this profiles skin face.
	 */
	/*public final ItemStack getLegacySkull() {
		ItemStack itemStack = new ItemStack(Items.SKULL, 1, 3);
		NbtCompound nbt = SimplifiedAPI.getNbtFactory().fromItemStack(itemStack);
		nbt.putPath("SkullOwner", this.getName());
		return itemStack;
	}*/

	/**
	 * Gets the unique identifier associated with this profile.
	 *
	 * @return Current profile UUID.
	 */
	public final UUID getUniqueId() {
		if (this.uuid == null)
			this.uuid = StringUtil.toUUID(this.id);

		return this.uuid;
	}

	/**
	 * Checks if this players profile is expired.
	 *
	 * @return True if expired, otherwise false.
	 */
	public final boolean hasExpired() {
		return System.currentTimeMillis() - this.updated >= 1800000;
	}

	@Override
	public final int hashCode() {
		return this.getUniqueId().hashCode();
	}

	/**
	 * Gets if the account is unpaid.
	 *
	 * @return True if unpaid, otherwise false.
	 */
	public final boolean isDemo() {
		return this.demo;
	}

	/**
	 * Gets if the account has not been migrated to Mojang.
	 *
	 * @return True if not migrated, otherwise false.
	 */
	public final boolean isLegacy() {
		return this.legacy;
	}

	/**
	 * Loads this profiles skin and cape.
	 * <p>
	 * This request is synchronous.
	 */
	public final void loadProperties() throws ProfileNotFoundException {
		final ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.UNIQUE_ID;

		try {
			if (MOJANG_API_AVAILABLE) {
				try {
					long wait = this.lastHttpRequest + WAIT_BETWEEN_REQUEST - System.currentTimeMillis();
					if (wait > 0) Thread.sleep(wait);
					HttpResponse response = HttpClient.get(MojangRepository.Services.getPropertiesUrl(this.getUniqueId()));

					if (HttpStatus.NO_CONTENT != response.getStatus()) {
						ProfileSearchResult result = GSON.fromJson(response.getBody().toString(), ProfileSearchResult.class);

						if (result != null) {
							JsonObject textures = result.getProperties().getValue().getAsJsonObject("textures");

							if (textures.has("SKIN"))
								this.skinUrl = new URL(textures.getAsJsonObject("SKIN").get("url").getAsString());

							if (textures.has("CAPE"))
								this.capeUrl = new URL(textures.getAsJsonObject("CAPE").get("url").getAsString());
						}
					}
				} catch (HttpConnectionException hcex) {
					if (HttpStatus.TOO_MANY_REQUESTS != hcex.getStatus())
						throw hcex;
				} finally {
					this.lastHttpRequest = System.currentTimeMillis();
				}
			}
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, this.getUniqueId());
		}

	}

	/**
	 * Loads this profiles skin and cape.
	 * <p>
	 * This request is asynchronous, it requires a callback if you wish to act upon the response.
	 *
	 * @param callback A call back used for when the request is finished.
	 */
	public final void loadProperties(Callback<MojangProfile, ProfileNotFoundException> callback) {
		Scheduler.getInstance().runAsync(() -> {
			ProfileNotFoundException pnfex = null;

			try {
				this.loadProperties();
			} catch (ProfileNotFoundException ex) {
				pnfex = ex;
			}

			if (callback != null)
				callback.handle(this, pnfex);
		});
	}

	@Override
	public final String toString() {
		return StringUtil.format("'{'{0},{1}'}'", this.getUniqueId(), this.getName());
	}

	@SuppressWarnings("unused")
	protected static class ProfileSearchResult {

		private String id;
		private String name;
		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		private List<PropertiesSearchResult> properties;

		public PropertiesSearchResult getProperties() {
			return this.properties.get(0);
		}

		protected static class PropertiesSearchResult {

			private String name;
			private String value;
			private String signature;

			public JsonObject getValue() {
				return new JsonParser().parse(new String(DataUtil.decode(this.value.toCharArray()), StandardCharsets.UTF_8)).getAsJsonObject();
			}

		}

	}


}