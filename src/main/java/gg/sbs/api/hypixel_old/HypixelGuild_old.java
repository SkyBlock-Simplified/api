package gg.sbs.api.hypixel_old;

import com.google.gson.annotations.SerializedName;
import gg.sbs.api.util.ListUtil;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import gg.sbs.api.util.concurrent.ConcurrentSet;

import java.util.UUID;

@SuppressWarnings("unused")
public final class HypixelGuild_old {

	private String _id;
	private String name;
	private String tag;
	private String description;
	private long created;
	private int exp;
	private boolean publiclyListed;
	private int coins;
	private int coinsEver;
	private ConcurrentMap<String, Integer> achievements;
	private ConcurrentMap<String, Integer> guildExpByGameType;
	private ConcurrentSet<String> preferredGames;
	private ConcurrentSet<Rank> ranks;

	public int getCoins() {
		return this.coins;
	}

	public int getCoinsEver() {
		return this.coinsEver;
	}

	public long getCreated() {
		return this.created;
	}

	public String getDescription() {
		return this.description;
	}

	public int getExperience() {
		return this.exp;
	}

	public ConcurrentMap<String, Integer> getExperienceByGameType() {
		return this.guildExpByGameType;
	}

	public String getName() {
		return this.name;
	}

	public ConcurrentSet<String> getPreferredGames() {
		return this.preferredGames;
	}

	public ConcurrentSet<Rank> getRanks() {
		return this.ranks;
	}

	public String getTag() {
		return this.tag;
	}

	public UUID getUniqueId() {
		return StringUtil.toUUID(this._id);
	}

	public boolean isPubliclyListed() {
		return this.publiclyListed;
	}

	public ConcurrentMap<String, Integer> getAchievements() {
		return this.achievements;
	}

	public class Member {

		private String uuid;
		private String rank;
		private long joined;
		private int questParticipation;
		private ConcurrentMap<String, Integer> expHistory;

		public ConcurrentMap<String, Integer> getExperienceHistory() {
			return this.expHistory;
		}

		public long getJoined() {
			return this.joined;
		}

		public int getQuestParticipation() {
			return this.questParticipation;
		}

		public Rank getRank() {
			return HypixelGuild_old.this.ranks.stream().filter(rank -> rank.getName().equals(this.rank) || ("Admin".equals(rank.getName()) && "Guild Master".equals(this.rank))).collect(ListUtil.toSingleton());
		}

		public UUID getUniqueId() {
			return StringUtil.toUUID(this.uuid);
		}

	}

	public static class Rank {

		private String name;
		private String tag;
		private long created;
		private int priority;
		@SerializedName("default") private boolean _default;

		public long getCreated() {
			return this.created;
		}

		public String getName() {
			return this.name;
		}

		public int getPriority() {
			return this.priority;
		}

		public String getTag() {
			return this.tag;
		}

		public boolean isDefault() {
			return this._default;
		}

	}

}