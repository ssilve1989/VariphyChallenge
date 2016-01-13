package com.variphy.challenge.filters;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.variphy.challenge.interfaces.BeerFilter;
import com.variphy.challenge.utils.BeerUtils;

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

			String name = BeerUtils.getAsString(obj, BeerUtils.NAME);
			String description = BeerUtils.getAsString(obj, BeerUtils.DESCRIPTION);

			final Matcher nameMatcher = keywordPattern.matcher(name);
			final Matcher descriptionMatcher = keywordPattern.matcher(description);

			if (!nameMatcher.find() && !descriptionMatcher.find()) {
				iterator.remove();
			}
		}
		return elements;
	}
}
