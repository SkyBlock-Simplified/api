package gg.sbs.api.hypixel_old;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import gg.sbs.api.hypixel_old.skyblock.RemoteLocation;
import gg.sbs.api.mojang.ChatFormatting;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.RegexUtil;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;

import java.lang.reflect.Type;
import java.util.UUID;

@SuppressWarnings("unused")
public final class HypixelProfile_old {

	private String _id;
	private String uuid;
	private String playername;
	private ConcurrentList<String> knownAliasesLower;
	private String displayname;
	private ConcurrentList<String> knownAliases;
	private long firstLogin;
	private long lastLogin;
	private long lastLogout;
	private long networkExp;
	private int karma;
	private int achievementPoints;
	private String mcVersionRp;
	private String mostRecentGameType;
	private Stats stats; // Fucking utter disaster
	private final long updated = System.currentTimeMillis();
	Session session;
	HypixelGuild_old hypixelGuild;

	private String packageRank;
	private String newPackageRank;
	private String monthlyPackageRank;
	private String rank;
	private String prefix;
	private String monthlyRankColor;
	private String rankPlusColor;

	public RankInfo getRank() {
		Rank rank = Rank.NONE;

		if (StringUtil.isNotEmpty(this.packageRank))
			rank = Rank.getRank(this.packageRank);

		if (StringUtil.isNotEmpty(this.newPackageRank))
			rank = Rank.getRank(this.newPackageRank);

		if (StringUtil.isNotEmpty(this.monthlyPackageRank) && !"NONE".equals(this.monthlyPackageRank))
			rank = Rank.getRank(this.monthlyPackageRank);

		if (StringUtil.isNotEmpty(this.rank) && !"NORMAL".equals(this.rank))
			rank = Rank.getRank(this.rank);

		if (StringUtil.isNotEmpty(this.prefix))
			rank = Rank.getRank(RegexUtil.strip(this.prefix, RegexUtil.VANILLA_PATTERN).replaceAll("[\\W]", ""));

		ChatFormatting rankFormat = rank.getDefaultFormat();
		ChatFormatting plusFormat = rank.getDefaultFormat();

		if (rank == Rank.SUPERSTAR && StringUtil.isNotEmpty(this.monthlyRankColor))
			rankFormat = ChatFormatting.valueOf(this.monthlyRankColor);

		if (StringUtil.isNotEmpty(this.rankPlusColor))
			plusFormat = ChatFormatting.valueOf(this.rankPlusColor);

		if (rank == Rank.PIG)
			plusFormat = ChatFormatting.AQUA;

		return new RankInfo(rank, rankFormat, plusFormat);
	}

	private HypixelProfile_old() { }

	public Session getSession() {
		return this.session;
	}

	public String getDisplayName() {
		return this.displayname;
	}

	public ConcurrentList<String> getKnownAliases() {
		return this.knownAliases;
	}

	public long getFirstLogin() {
		return this.firstLogin;
	}

	public HypixelGuild_old getGuild() {
		return this.hypixelGuild;
	}

	public long getLastLogin() {
		return this.lastLogin;
	}

	public String getName() {
		return this._id;
	}

	public UUID getUniqueId() {
		return StringUtil.toUUID(this.uuid);
	}

	public SkyblockProfile getSkyblockProfile(UUID profileId) {
		for (SkyblockProfile profile : this.getSkyblockProfiles()) {
			if (profile.getUniqueId().equals(profileId))
				return profile;
		}

		throw new IllegalArgumentException(FormatUtil.format("Profile ID {0} is not part of {0}''s Hypixel Profile!", profileId, this.getDisplayName()));
	}

	public ConcurrentList<SkyblockProfile> getSkyblockProfiles() {
		return this.stats.skyblock.profiles;
	}

	public SkyblockProfile getFirstSkyblockProfile() {
		return this.getSkyblockProfiles().get(0);
	}

	@Override
	public final int hashCode() {
		return this.getUniqueId().hashCode();
	}

	public boolean hasAnySkyblockProfile() {
		return this.hasPlayedAnyGames() && !this.stats.skyblock.profiles.isEmpty();
	}

	/**
	 * Checks if this players profile is expired.
	 *
	 * @return True if expired, otherwise false.
	 */
	public final boolean hasExpired() {
		return System.currentTimeMillis() - this.updated >= 1800000;
	}

	public boolean hasPlayedAnyGames() {
		return this.stats != null;
	}

	public boolean isOnSkyblockIsland(SkyblockIsland skyblockIsland) {
		return this.isOnSkyblockIsland(skyblockIsland.getIslandId());
	}

	public boolean isOnSkyblockIsland(UUID islandId) {
		for (SkyblockProfile profile : this.getSkyblockProfiles()) {
			if (profile.getUniqueId().equals(islandId))
				return true;
		}

		return false;
	}

	public enum Rank {

		OWNER(ChatFormatting.RED),
		ADMIN(ChatFormatting.RED),
		BUILD_TEAM(ChatFormatting.DARK_AQUA),
		MODERATOR(ChatFormatting.DARK_GREEN, "MOD"),
		HELPER(ChatFormatting.BLUE),
		JR_HELPER(ChatFormatting.BLUE),
		YOUTUBER(ChatFormatting.RED, "YOUTUBE"),
		SUPERSTAR(ChatFormatting.GOLD, "MVP", 2),
		MVP_PLUS(ChatFormatting.AQUA, "MVP", 1),
		MVP(ChatFormatting.AQUA),
		VIP_PLUS(ChatFormatting.GREEN, "VIP", 1),
		VIP(ChatFormatting.GREEN),
		PIG(ChatFormatting.LIGHT_PURPLE, "PIG", 3),
		NONE(ChatFormatting.GRAY);

		private final ChatFormatting format;
		private final String name;
		private final int plusCount;

		Rank(ChatFormatting format) {
			this(format, null);
		}

		Rank(ChatFormatting format, String name) {
			this(format, name, 0);
		}

		Rank(ChatFormatting format, String name, int plusCount) {
			this.format = format;
			this.name = (StringUtil.isEmpty(name) ? name() : name).replace("_", " ");
			this.plusCount = plusCount;
		}

		public ChatFormatting getDefaultFormat() {
			return this.format;
		}

		public String getName() {
			return this.name;
		}

		public int getPlusCount() {
			return this.plusCount;
		}

		public static Rank getRank(String name) {
			name = name.replaceAll("\\+", "");

			for (Rank rank : values()) {
				if (rank.name().equals(name))
					return rank;
			}

			return Rank.NONE;
		}

	}

	public static class RankInfo {

		private final Rank rank;
		private final ChatFormatting rankFormat;
		private final ChatFormatting plusFormat;
		private final String pluses;

		RankInfo(Rank rank, ChatFormatting rankFormat, ChatFormatting plusFormat) {
			this.rank = rank;
			this.rankFormat = rankFormat;
			this.plusFormat = plusFormat;

			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < this.getRank().getPlusCount(); i++) builder.append("+");
			this.pluses = builder.toString();
		}

		public Rank getRank() {
			return this.rank;
		}

		public String getPluses() {
			return this.pluses;
		}

		public ChatFormatting getRankFormat() {
			return this.rankFormat;
		}

		public ChatFormatting getPlusFormat() {
			return this.plusFormat;
		}

		@Override
		public String toString() {
			String sfPluses = FormatUtil.preformat("{0}", this.getPlusFormat(), this.getPluses());
			String sfRank = FormatUtil.preformat("{0}", this.getRankFormat(), this.getRank().getName());
			return FormatUtil.format("{0}[{1}{2}{3}]", ChatFormatting.WHITE.toString(), sfRank, sfPluses, ChatFormatting.WHITE.toString());
		}

	}

	public static class Session {

		private boolean online;
		private String gameType;
		private String mode;

		public boolean isOnline() {
			return this.online;
		}

		public String getGameType() {
			return this.gameType;
		}

		public String getMode() {
			return this.mode;
		}

		public RemoteLocation getRemoteLocation() {
			return this.isOnline() ? RemoteLocation.getRemoteLocation(this.getMode()) : RemoteLocation.OFFLINE;
		}

		public boolean isOnSkyblock() {
			return "SKYBLOCK".equals(this.getGameType());
		}

	}

	public static class SkyblockProfile {

		private final UUID uniqueId;
		private final String cuteName;

		SkyblockProfile(UUID uniqueId, String cuteName) {
			this.uniqueId = uniqueId;
			this.cuteName = cuteName;
		}

		public String getCuteName() {
			return this.cuteName;
		}

		public UUID getUniqueId() {
			return this.uniqueId;
		}

	}

	static class Stats {

		@SerializedName("SkyBlock")
		private SkyblockStats skyblock;

		static class SkyblockStats {

			private ConcurrentList<SkyblockProfile> profiles = Concurrent.newList();

		}

		static class Deserializer implements JsonDeserializer<SkyblockStats> {

			// Use a new instance of Gson to avoid infinite recursion to a deserializer.
			@Override
			public SkyblockStats deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException {
				try {
					JsonObject profiles = jsonElement.getAsJsonObject().getAsJsonObject("profiles");
					SkyblockStats skyblock = new SkyblockStats();
					profiles.entrySet().forEach(entry -> skyblock.profiles.add(new SkyblockProfile(StringUtil.toUUID(entry.getKey()), entry.getValue().getAsJsonObject().get("cute_name").getAsString())));
					return skyblock;
				} catch (Exception ex) {
					throw new HypixelApiException(HypixelApiException.Reason.NO_EXISTING_PLAYER, HypixelAPI.Endpoint.PLAYER, "This player has never played Skyblock!", ex);
				}
			}

		}

	}

}