package com.variphy.challenge.interfaces;

import java.util.List;

import com.google.gson.JsonObject;

public interface BeerFilter {
	List<JsonObject> filter(List<JsonObject> elements);
}
