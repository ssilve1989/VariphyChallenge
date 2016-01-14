package com.variphy.challenge.filters;

import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonObject;
import com.variphy.challenge.interfaces.BeerFilter;
import com.variphy.challenge.utils.BeerUtils;

/**
 * Filters out any items that don't meet the minimum requirements
 */
public class ABVFilter implements BeerFilter {

	//Using the Float wrapper here so we can pass in nulls to the constructor
	private final Float minABV;
	private final Float maxABV;

	public ABVFilter(Float minABV, Float maxABV) {
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

			Float oAbvMin, oAbvMax;

			oAbvMin = BeerUtils.getAsFloat(obj, BeerUtils.MIN_ABV);
			oAbvMax = BeerUtils.getAsFloat(obj, BeerUtils.MAX_ABV);

			if ((oAbvMin == null || oAbvMax == null)) {
				if (obj.has(BeerUtils.ABV)) {
					//use abv as the sole value
					oAbvMax = oAbvMin = BeerUtils.getAsFloat(obj, BeerUtils.ABV);
				}
			}

			if ((oAbvMax != null) && oAbvMin != null) {
				if (this.minABV != null && this.maxABV != null) {
					remove = (this.minABV.compareTo(oAbvMin) == 1) || (this.maxABV.compareTo(oAbvMax) == -1);
				}
				else if (this.minABV != null) {
					remove = (this.minABV.compareTo(oAbvMin) == 1 && this.minABV.compareTo(oAbvMax) == 1);
				}
				else {
					remove = (this.maxABV.compareTo(oAbvMax) == -1);
				}
			}

			if (remove) {
				iterator.remove();
			}
		}

		return elements;
	}
}
