package dev.sbs.api.util.comparator;

import java.util.Comparator;

public class LengthCompare implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		return s1.length() - s2.length();
	}

}
