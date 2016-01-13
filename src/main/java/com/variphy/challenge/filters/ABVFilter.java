package com.variphy.challenge.filters;

import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonObject;
import com.variphy.challenge.App;
import com.variphy.challenge.config.AppConfig;
import com.variphy.challenge.interfaces.BeerFilter;

/**
 * Filters out any items that don't meet the minimum requirements
 */
public class ABVFilter implements BeerFilter {

	//Using the Float wrapper here so we can pass in nulls to the constructor
	private final Float minABV;
	private final Float maxABV;

	public ABVFilter(Float minABV, Float maxABV){
		this.minABV = minABV;
		this.maxABV = maxABV;
	}

	@Override
	public List<JsonObject> filter(List<JsonObject> elements) {
		//iterate through removing as needed
		Iterator<JsonObject> iterator = elements.iterator();
		while (iterator.hasNext()) {
			boolean remove = false;

			final JsonObject obj = iterator.next();
			JsonObject style = null;

			Float oAbvMin = null, oAbvMax = null;

			if(obj.has("style")){
				style = obj.get("style").getAsJsonObject();

				if(style.has(AppConfig.MIN_ABV_OPTION) && style.has(AppConfig.MAX_ABV_OPTION)){
					oAbvMin = style.get(AppConfig.MIN_ABV_OPTION).getAsFloat();
					oAbvMax = style.get(AppConfig.MAX_ABV_OPTION).getAsFloat();
				}
			}
			if(oAbvMin == null){
				if(obj.has("abv")) {
					//use abv as the sole value
					oAbvMax = oAbvMin = obj.get("abv").getAsFloat();
				}else{
					//no abv value could be determined, leave it alone
					continue;
				}
			}

			if(this.minABV != null && this.maxABV != null){
				remove = (this.minABV.compareTo(oAbvMin) == 1) || (this.maxABV.compareTo(oAbvMax) == -1);
			}else if(this.minABV != null){
				remove = (this.minABV.compareTo(oAbvMin) == 1 && this.minABV.compareTo(oAbvMax) == 1);
			}else{
				remove = (this.maxABV.compareTo(oAbvMax) == -1);
			}

			if(remove){
				iterator.remove();
			}
		}

		return elements;
	}
}
