package gg.sbs.api.mojang_old.exceptions;

import gg.sbs.api.mojang_old.MojangProfile;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.StringUtil;

/**
 * Custom exception for use by the MojangRepository
 */
public class ProfileNotFoundException extends RuntimeException {

	private final String details;
	private final Reason reason;
	private final LookupType type;

	/**
	 * Create a new exception instance.
	 *
	 * @param reason Mojang response type.
	 * @param type   Lookup request type.
	 * @param obj    Object to be used in the exception message.
	 */
	public ProfileNotFoundException(Reason reason, LookupType type, Object obj) {
		this(reason, type, null, obj);
	}

	/**
	 * Create a new exception instance.
	 *
	 * @param reason    Mojang response type.
	 * @param throwable Error that occured.
	 * @param type      Lookup request type.
	 * @param obj       Object to be used in the exception message.
	 */
	public ProfileNotFoundException(Reason reason, LookupType type, Throwable throwable, Object obj) {
		super(throwable == null ? getCustomMessage(type, obj) : FormatUtil.format("{0}: {1}: {2}", getCustomMessage(type, obj), throwable.getClass().getName(), throwable.getMessage()), throwable);
		this.reason = reason;
		this.type = type;
		this.details = getCustomMessage(this.type, obj);
	}

	public String getDetails() {
		return this.details;
	}

	public Reason getReason() {
		return this.reason;
	}

	public LookupType getType() {
		return this.type;
	}

	public enum Reason {

		API_UNAVAILABLE("Mojang API is Unavailable!"),
		INVALID_PLAYER("Unable to locate profile data for the provided player!"),
		EXCEPTION("An unknown error has occurred!"),
		RATE_LIMITED("You have been rate limited!");

		private final String cause;

		Reason(String cause) {
			this.cause = cause;
		}

		public String getCause() {
			return this.cause;
		}

	}

	public enum LookupType {

		OFFLINE_PLAYERS,
		OFFLINE_PLAYER,
		UNIQUE_ID,
		USERNAMES,
		USERNAME

	}

	private static String getCustomMessage(LookupType type, Object obj) {
		switch (type) {
			case OFFLINE_PLAYERS:
				StringBuilder players = new StringBuilder();
				MojangProfile[] profiles = (MojangProfile[])obj;

				for (MojangProfile profile : profiles)
					players.append(FormatUtil.format("'{'{0},{1}'}'", profile.getUniqueId(), profile.getName()));

				return FormatUtil.format("The profile data for offline players '{'{0}'}' could not be found!", players.toString());
			case OFFLINE_PLAYER:
				MojangProfile profile = (MojangProfile)obj;
				return FormatUtil.format("The profile data for offline player '{'{0},{1}'}' could not be found!", profile.getUniqueId(), profile.getName());
			case UNIQUE_ID:
				return FormatUtil.format("The profile data for uuid ''{0}'' could not be found!", obj);
			case USERNAMES:
				return FormatUtil.format("The profile data for users '{'{0}'}' could not be found!", StringUtil.join(", ", (String[])obj));
			case USERNAME:
				return FormatUtil.format("The profile data for user ''{0}'' could not be found!", obj);
			default:
				return FormatUtil.format("The profile data for ''{0}'' could not be found!", obj);
		}
	}

}