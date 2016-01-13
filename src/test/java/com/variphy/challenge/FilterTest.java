package com.variphy.challenge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.variphy.challenge.filters.ABVFilter;
import com.variphy.challenge.filters.KeywordFilter;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FilterTest {

	JsonParser jsonParser;
	List<JsonObject> elements;

	@Before
	public void initialize() throws IOException {
		jsonParser = new JsonParser();
		elements = new LinkedList<>();

		//Java 8 Streaming API
		String rawJson = Files.lines(Paths.get("./resources/data.json"))
		                      .parallel()
		                      .map(String::trim)
		                      .collect(Collectors.joining());

		JsonArray root = jsonParser.parse(rawJson).getAsJsonObject().get("data").getAsJsonArray();

		for (JsonElement e : root) {
			elements.add(e.getAsJsonObject());
		}
	}

	@Test
	public void testKeywordFilter() {
		KeywordFilter keywordFilter = new KeywordFilter("eiusmod");

		List<JsonObject> filtered = keywordFilter.filter(elements);

		assertEquals(2, filtered.size());
	}

	//Gonna waste memory and check all my use cases in one method
	@Test
	public void testABVFilter() {
		JsonObject obj = new JsonObject();
		JsonObject style = new JsonObject();

		style.addProperty("abvMin", 5.00F);
		style.addProperty("abvMax", 10.00F);

		obj.add("style", style);

		List<JsonObject> elements = new ArrayList<>();
		elements.add(obj);

		ABVFilter abvFilter = new ABVFilter(3.0F, null);
		assertEquals(1, abvFilter.filter(new ArrayList<>(elements)).size());

		abvFilter = new ABVFilter(6.1F, null);
		assertEquals(1, abvFilter.filter(new ArrayList<>(elements)).size());

		abvFilter = new ABVFilter(3.0F, 8.0F);
		assertEquals(0, abvFilter.filter(new ArrayList<>(elements)).size());

		abvFilter = new ABVFilter(3.0F, 11.0F);
		assertEquals(1, abvFilter.filter(new ArrayList<>(elements)).size());

		abvFilter = new ABVFilter(null, 8.0F);
		assertEquals(0, abvFilter.filter(new ArrayList<>(elements)).size());

		abvFilter = new ABVFilter(null, 11.0F);
		assertEquals(1, abvFilter.filter(new ArrayList<>(elements)).size());
	}
}
