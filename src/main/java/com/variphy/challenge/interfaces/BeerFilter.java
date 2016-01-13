package com.variphy.challenge.interfaces;

import java.util.List;

import com.google.gson.JsonObject;

/**
 * Created by Steven Silvestri on 1/12/16.
 *
 * Users of the service should define how they want to filter it themselves.
 */
public interface BeerFilter {

	List<JsonObject> filter(List<JsonObject> elements);
}
