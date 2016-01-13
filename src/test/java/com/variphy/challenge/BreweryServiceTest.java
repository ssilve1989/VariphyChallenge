package com.variphy.challenge;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.variphy.challenge.comparator.BeerDateComparator;
import com.variphy.challenge.config.AppConfig;
import com.variphy.challenge.services.BreweryService;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BreweryServiceTest {

	public static final String sUrl = "http://api.brewerydb.com/v2/brewery/RzvedX?key=" + AppConfig.API_KEY;

	URL url;
	HttpURLConnection conn;
	JsonParser jsonParser;

	@Before
	public void initialize() throws IOException {
		jsonParser = new JsonParser();
	}

	@Test
	public void apiRequest() throws IOException {
		url = new URL(sUrl);
		conn = (HttpURLConnection) url.openConnection();
		conn.connect();

		JsonElement root = jsonParser.parse(new InputStreamReader((InputStream) conn.getContent()));
		JsonObject rootobj = root.getAsJsonObject();

		assertEquals(rootobj.get("message").toString(), "\"Request Successful\"");
	}

	@Test
	public void getBeersByBrewery() {
		BreweryService breweryService = BreweryService.getService();
		JsonElement jsonElement = breweryService.getBeersByBrewery("RzvedX");

		assertNotNull(jsonElement.getAsJsonObject().get("data"));
	}

	@Test
	public void testSortByDate() {
		BreweryService breweryService = BreweryService.getService();
		JsonElement root = breweryService.getBeersByBrewery(AppConfig.BREWERY_ID);
		JsonArray data = root.getAsJsonObject().get("data").getAsJsonArray();

		List<JsonObject> list = new LinkedList<>();

		for (JsonElement e : data) {
			//test the abv values
			list.add(e.getAsJsonObject());
		}

		//Sort newest first by date
		Collections.sort(list, Collections.reverseOrder(new BeerDateComparator()));

		for (JsonObject beer : list) {
			System.out.println(beer.get("createDate").getAsString());
		}

		//TODO assert the sort was correct

	}
}
