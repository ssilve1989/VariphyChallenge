package com.variphy.challenge.comparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.google.gson.JsonObject;

/**
 * Created by Steven Silvestri on 1/12/16.
 *
 * Notes:
 *
 * Not a fan of throwing exceptions here. Another approach would be to see which object caused the problem
 * and then shift that object to either the front or back of the collection.
 */
public class BeerDateComparator implements Comparator<JsonObject> {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");

	@Override
	public int compare(final JsonObject o1, final JsonObject o2) {
		try {
			Date d1 = dateFormat.parse(o1.get("createDate").getAsString());
			Date d2 = dateFormat.parse(o2.get("createDate").getAsString());

			return d1.compareTo(d2);
		}
		catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
