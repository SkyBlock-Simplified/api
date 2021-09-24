package gg.sbs.api.hypixel_old.skyblock;

public enum RemoteLocation {

	// General
	LOBBY("LOBBY", "Network Lobby"),
	OTHER_GAME(null, "Other Game"),
	OFFLINE(null, "Offline"),

	// Skyblock General
	PRIVATE_ISLAND("dynamic", "Private Island"),
	HUB("hub", "Hub"),
	DARK_AUCTION("dark_auction", Location.DARK_AUCTION),
	DUNGEON("dungeon", "Dungeons"),

	// Mining
	GOLD_MINE("mining_1", Location.GOLD_MINE),
	DEEP_CAVERNS("mining_2", Location.DEEP_CAVERNS),

	// Combat
	SPIDERS_DEN("combat_1", Location.SPIDERS_DEN),
	BLAZING_FORTRESS("combat_2", Location.BLAZING_FORTRESS),
	THE_END("combat_3", Location.THE_END),

	// Farming
	THE_BARN("farming_1", Location.THE_BARN),
	MUSHROOM_DESERT("farming_2", Location.MUSHROOM_DESERT),

	// Foraging
	THE_PARK("foraging_1", "The Park"),

	// Jerry's Workshop
	JERRYS_WORKSHOP("winter", Location.JERRYS_WORKSHOP);

	private final String apiName;
	private final String scoreboardName;

	RemoteLocation(String apiName, Location location) {
		this(apiName, location.getScoreboardName());
	}

	RemoteLocation(String apiName, String scoreboardName) {
		this.apiName = apiName;
		this.scoreboardName = scoreboardName;
	}

	public String getApiName() {
		return this.apiName;
	}

	public String getScoreboardName() {
		return this.scoreboardName;
	}

	public static RemoteLocation getRemoteLocation(String apiName) {
		for (RemoteLocation value : values()) {
			if (value == OTHER_GAME || value == OFFLINE)
				continue;

			if (value.getApiName().equals(apiName))
				return value;
		}

		return OTHER_GAME;
	}

}