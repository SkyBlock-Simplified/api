package gg.sbs.api.util;

import java.util.Map;

public interface Serializable {

	/**
	 * Serialize the current object into a map.
	 *
	 * @return The serialized object.
	 */
	Map<String, Object> serialize();

}