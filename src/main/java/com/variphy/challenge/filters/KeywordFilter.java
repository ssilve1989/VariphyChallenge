package com.variphy.challenge.filters;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.variphy.challenge.interfaces.BeerFilter;

/**
 * Filters by keyword appearing in the name or description of the beer. It will match based on the occurence
 * of the entire keyword token. Partial matching could be implemented via Pattern matching or String.contains()
 * if desired and would be another Filter implementing BeerFilter
 */
public class KeywordFilter implements BeerFilter {

	private final String keyword;

	public KeywordFilter(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * Performs a case-insensitive keyword search
	 *
	 * @param elements
	 * @return a filtered linkedlist where non-matching items have been removed
	 */
	@Override
	public List<JsonObject> filter(List<JsonObject> elements) {

		Pattern keywordPattern = Pattern.compile("\\b" + this.keyword + "\\b", Pattern.CASE_INSENSITIVE);

		Iterator<JsonObject> iterator = elements.iterator();

		while (iterator.hasNext()) {
			final JsonObject obj = iterator.next();
			JsonObject style = null;

			String name = "";
			String description = "";

			if (obj.has("style")) {
				style = obj.get("style").getAsJsonObject();
			}

			if (obj.has("name")) {
				name = obj.get("name").getAsString();
			}

			if (obj.has("description")) {
				description = obj.get("description").getAsString();
			}

			if (name.isEmpty()) {
				if (style != null) {
					name = style.get("name").getAsString();
				}
			}

			if (description.isEmpty()) {
				if (style != null) {
					description = style.get("description").getAsString();
				}
			}

			final Matcher nameMatcher = keywordPattern.matcher(name);
			final Matcher descriptionMatcher = keywordPattern.matcher(description);

			if (!nameMatcher.find() && !descriptionMatcher.find()) {
				iterator.remove();
			}
		}
		return elements;
	}
}
