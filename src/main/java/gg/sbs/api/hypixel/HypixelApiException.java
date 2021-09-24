package gg.sbs.api.hypixel;

import gg.sbs.api.util.FormatUtil;

/**
 * Custom exception for use by the HypixelAPI
 */
public class HypixelApiException extends RuntimeException {

	private final String details;
	private final Reason reason;
	private final String message;
	private final HypixelAPI.Endpoint endpoint;

	/**
	 * Create a new exception instance.
	 *
	 * @param reason Mojang response type.
	 * @param endpoint Lookup request type.
	 */
	public HypixelApiException(Reason reason, HypixelAPI.Endpoint endpoint, String message) {
		this(reason, endpoint, message, null);
	}

	/**
	 * Create a new exception instance.
	 *
	 * @param reason Mojang response type.
	 * @param throwable Error that occured.
	 * @param endpoint Lookup request type.
	 */
	public HypixelApiException(Reason reason, HypixelAPI.Endpoint endpoint, String message, Throwable throwable) {
		super(throwable == null ? getCustomMessage(endpoint, message) : FormatUtil.format("{0}: {1}: {2}", getCustomMessage(endpoint, message), throwable.getClass().getName(), throwable.getMessage()), throwable);
		this.reason = reason;

		this.endpoint = endpoint;
		this.message = message;
		this.details = getCustomMessage(this.endpoint, this.message);
	}

	public String getDetails() {
		return this.details;
	}

	public Reason getReason() {
		return this.reason;
	}

	public HypixelAPI.Endpoint getEndpoint() {
		return this.endpoint;
	}

	public enum Reason {

		INVALID_API_KEY("Invalid API key!"),
		NO_EXISTING_PLAYER("Unable to locate profile data for a valid player!"),
		EXCEPTION("An unknown error has occurred!"),
		API_EXCEPTION("You are using the API incorrectly!");

		private final String cause;

		Reason(String cause) {
			this.cause = cause;
		}

		public String getCause() {
			return this.cause;
		}

	}

	private static String getCustomMessage(HypixelAPI.Endpoint endpoint, String message) {
		return FormatUtil.format("The Hypixel API Endpoint ''{0}'' has encountered a problem! ({1})", endpoint.getEndpoint(), message);
	}

}