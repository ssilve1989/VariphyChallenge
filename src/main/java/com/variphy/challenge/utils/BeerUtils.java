package com.variphy.challenge.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Utility class to consolidate the logic for falling back on the style property if
 * no value is found on the main object.
 *
 * I made this because it was pretty annoying to constantly have to check the style object as the fallback
 */
public class BeerUtils {

	// I decided to store some attributes here when thinking what if the 3rd party api changes its field names?
	// TODO: Update all occurences of the strings to reference these where it makes sense
	public static final String STYLE = "style";
	public static final String ABV = "abv";
	public static final String IBU = "ibu";
	public static final String MIN_ABV = "abvMin";
	public static final String MAX_ABV = "abvMax";
	public static final String MIN_IBU = "ibuMin";
	public static final String MAX_IBU = "ibuMax";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";

	public static JsonElement get(final JsonObject obj, String attribute) {
		if (obj.has(attribute)) {
			JsonElement val = obj.get(attribute);
			//make sure it wasn't empty
			if (!"".equals(val.getAsString())) {
				return obj.get(attribute);
			}
		}

		if (obj.has(STYLE)) {
			JsonObject style = obj.get(STYLE).getAsJsonObject();
			if (style.has(attribute)) {
				return style.get(attribute);
			}
		}
		return null;
	}

	//Convenience methods
	//I only defined the ones I was using but ideally would all cases should be here
	public static Float getAsFloat(final JsonObject obj, String attribute) {
		JsonElement e = get(obj, attribute);
		return e == null ? null : e.getAsFloat();
	}

	public static String getAsString(final JsonObject obj, String attribute) {
		JsonElement e = get(obj, attribute);
		return e == null ? "" : e.getAsString();
	}

	public static Float getABV(final JsonObject object) {
		if (object.has(ABV)) {
			return object.get(ABV).getAsFloat();
		}
		if (object.has(STYLE)) {
			JsonObject style = object.get(STYLE).getAsJsonObject();
			if (style.has(MAX_ABV) && style.has(MIN_ABV)) {
				Float min = style.get(MIN_ABV).getAsFloat();
				Float max = style.get(MAX_ABV).getAsFloat();
				return ((min + max) / 2.00F);
			}
			else {
				return null;
			}
		}
		return null;
	}

	public static Float getIBU(final JsonObject object) {
		if (object.has(IBU)) {
			return object.get(IBU).getAsFloat();
		}
		if (object.has(STYLE)) {
			JsonObject style = object.get(STYLE).getAsJsonObject();
			if (style.has(MAX_IBU) && style.has(MIN_IBU)) {
				Float min = style.get(MIN_IBU).getAsFloat();
				Float max = style.get(MAX_IBU).getAsFloat();
				return ((min + max) / 2.00F);
			}
			else {
				return null;
			}
		}
		return null;
	}
}
