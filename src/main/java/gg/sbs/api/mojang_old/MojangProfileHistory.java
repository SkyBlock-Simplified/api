package gg.sbs.api.mojang_old;

public class MojangProfileHistory {

	protected long changedToAt;
	protected String name;

	/**
	 * Gets the timestamp when this name was created.
	 *
	 * @return Current time profile was created (Or 0 if it's the first name).
	 */
	public final long getChangedToAt() {
		return this.changedToAt;
	}

	/**
	 * Gets the name associated with this profile.
	 *
	 * @return Current profile name.
	 */
	public final String getName() {
		return this.name;
	}

}