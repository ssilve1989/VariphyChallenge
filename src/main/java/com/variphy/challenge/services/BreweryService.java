package com.variphy.challenge.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.variphy.challenge.config.AppConfig;

/**
 * Notes: I don't bother storing the endpoints as static final vars since they are added to the string pool
 * anyway and only used in this class.
 *
 * I designed this service to handle its own exceptions since the exceptions would not be caused by external input to the class
 * but rather internally.
 *
 * It also will not sort the data. The user of this service can decide how it wants to sort and present the data.
 *
 * If I wanted to be extremely picky about memory management I would not append any strings here and use a single instance
 * of StringBuilder and setLength(0) after each API Request.
 */
public class BreweryService {

	private static final String API_URL = "http://api.brewerydb.com/v2/";
	private static final JsonObject EMPTY_OBJECT = new JsonObject();

	private static BreweryService service; //Singleton Instance
	private final JsonParser jsonParser;

	private BreweryService() {
		jsonParser = new JsonParser();
	}

	public static BreweryService getService() {
		if (service == null) {
			service = new BreweryService();
		}
		return service;
	}

	public JsonElement getBeersByBrewery(String breweryId) {
		String endpoint = "/brewery/" + breweryId + "/beers" + "?key=" + AppConfig.API_KEY;
		return this.makeRequest(endpoint);
	}

	private JsonElement makeRequest(String endpoint) {
		String sUrl = API_URL + endpoint;
		HttpURLConnection request = null;

		try {
			URL url = new URL(sUrl);
			request = (HttpURLConnection) url.openConnection();
			return this.jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (request != null) {
				request.disconnect();
			}
		}
		//If we made it here return a default json object
		return EMPTY_OBJECT;
	}
}
