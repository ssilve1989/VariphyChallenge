package com.variphy.challenge.comparator;

import java.util.Comparator;

import com.google.gson.JsonObject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BeerDateComparator implements Comparator<JsonObject> {

	private final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public int compare(final JsonObject o1, final JsonObject o2) {
		DateTime d1 = dateFormat.parseDateTime(o1.get("createDate").getAsString());
		DateTime d2 = dateFormat.parseDateTime(o2.get("createDate").getAsString());

		return d1.compareTo(d2);
	}
}
