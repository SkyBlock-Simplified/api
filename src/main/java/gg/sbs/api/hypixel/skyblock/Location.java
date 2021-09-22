package gg.sbs.api.hypixel.skyblock;

public enum Location {

	YOUR_ISLAND("Your Island"),
	VISITING_ISLAND("Island"),
	DARK_AUCTION("Dark Auction"),
	NONE("None"),
	UNKNOWN(null),

	// Hub
	VILLAGE("Village"),
	AUCTION_HOUSE("Auction House"),
	BAZAAR_ALLEY("Bazaar Alley"),
	BANK("Bank"),
	LIBRARY("Library"),
	COAL_MINE("Coal Mine"),
	GRAVEYARD("Graveyard"),
	COLOSSEUM("Colosseum"),
	WILDERNESS("Wilderness"),
	MOUNTAIN("Mountain"),
	WIZARD_TOWER("Wizard Tower"),
	RUINS("Ruins"),
	FOREST("Forest"),
	FARM("Farm"),
	FISHERMANS_HUT("Fisherman's Hut"),
	HIGH_LEVEL("High Level"),
	FLOWER_HOUSE("Flower House"),
	CANVAS_ROOM("Canvas Room"),
	TAVERN("Tavern"),
	COMMUNITY_CENTER("Community Center"),

	// Dungeons
	CATACOMBS_ENTRANCE("Catacombs Entrance"),
	DUNGEON_HUB("Dungeon Hub"),
	THE_CATACOMBS("The Catacombs"),

	// Floating Islands
	BIRCH_PARK("Birch Park"),
	SPRUCE_WOODS("Spruce Woods"),
	JUNGLE_ISLAND("Jungle Island"),
	SAVANNA_WOODLAND("Savanna Woodland"),
	DARK_THICKET("Dark Thicket"),
	HOWLING_CAVE("Howling Cave"),

	// Gold Mine
	GOLD_MINE("Gold Mine"),

	// Deep Caverns
	DEEP_CAVERNS("Deep Caverns"),
	GUNPOWDER_MINES("Gunpowder Mines"),
	LAPIS_QUARRY("Lapis Quarry"),
	PIGMAN_DEN("Pigmen's Den"),
	SLIMEHILL("Slimehill"),
	DIAMOND_RESERVE("Diamond Reserve"),
	OBSIDIAN_SANCTUARY("Obsidian Sanctuary"),

	// The Barn
	THE_BARN("The Barn"),

	// Mushroom Desert
	MUSHROOM_DESERT("Mushroom Desert"),

	//Spider Den
	SPIDERS_DEN("Spider's Den"),

	// Blazing fortress
	BLAZING_FORTRESS("Blazing Fortress"),

	// End
	THE_END("The End"),
	DRAGONS_NEST("Dragon's Nest"),

	// Jerry's Workshop
	JERRYS_WORKSHOP("Jerry's Workshop");

	private String scoreboardName;

	Location(String scoreboardName) {
		this.scoreboardName = scoreboardName;
	}

	public String getScoreboardName() {
		return scoreboardName;
	}

}