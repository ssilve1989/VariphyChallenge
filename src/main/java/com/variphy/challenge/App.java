package com.variphy.challenge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.variphy.challenge.comparator.BeerDateComparator;
import com.variphy.challenge.config.AppConfig;
import com.variphy.challenge.filters.ABVFilter;
import com.variphy.challenge.filters.KeywordFilter;
import com.variphy.challenge.services.BreweryService;
import com.variphy.challenge.utils.BeerUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Write a Java program or application, using any type of Java libraries or technologies that you wish,
 * that accepts a minimum and/or maximum ABV as well as an optional search keyword and returns/displays the
 * following information for beers made by the Harpoon Brewery (ID = RzvedX) matching the specified search criteria.
 *
 * The search keyword should be used to search by the beer name or description values.
 * The search results should be displayed in descending order (newest first) by Create Date.
 *
 * If the API doesn't return a necessary value (such as ABV or description) for a beer,
 * then a suitable value from the element/property "style" should be used instead.
 *
 * Notes:
 *
 * 1) Ideally I would include a logging framework for log statements.
 * 3) The application checks if there is at least one argument min/max but retrieves both anyway. This is just for
 * execution validation purposes since if one does happen to be null, the filters will handle them safely. Also if they
 * happen to be unparseable to Float they will be safely handled as well.
 */
public class App {

	//private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
	private static final DateTimeFormatter dateParsers = AppConfig.dateParsers;
	private static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("MM/dd/yyyy");

	private static final Calendar cal = Calendar.getInstance();

	public static void main(String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine commandLine = parser.parse(AppConfig.options, args);

			if (hasRequiredArgs(commandLine)) {
				Float minABV = commandLine.hasOption(AppConfig.MIN_ABV) ?
				               Float.parseFloat(commandLine.getOptionValue(AppConfig.MIN_ABV)) : null;

				Float maxABV = commandLine.hasOption(AppConfig.MAX_ABV) ?
				               Float.parseFloat(commandLine.getOptionValue(AppConfig.MAX_ABV)) : null;

				BreweryService breweryService = BreweryService.getService();

				//Load up the beers for the Harpoon Brewery
				JsonElement root = breweryService.getBeersByBrewery(AppConfig.BREWERY_ID);
				JsonObject rootObj = root.getAsJsonObject();

				if (rootObj.has("data")) {
					JsonArray data = rootObj.get("data").getAsJsonArray();
					//Extract into a Java Collection since there is no easy way to sort these data structures
					List<JsonObject> elements = new LinkedList<>();

					for (JsonElement d : data) {
						//assuming that the contents of the data array are always json objects and not arrays
						elements.add(d.getAsJsonObject());
					}

					data = null; //Free up this object for the next GC since we don't need it anymore.

				    /*
				        The filters are separated to promote larger flexibility throughout a potentially
				        larger use case. It is inefficient if we only care about this one very specific use case to filter
				        by search and min/max abv.

				        The filters are also filtering in-place on the same linked-list structure which provides fast removal
				        so that should make up for it somewhat.
				     */

					//filter before sorting
					ABVFilter abvFilter = new ABVFilter(minABV, maxABV);
					elements = abvFilter.filter(elements);

					//If there was a search keyword we now want to filter again
					if (commandLine.hasOption(AppConfig.KEYWORD_OPTION)) {
						String keyword = commandLine.getOptionValue(AppConfig.KEYWORD_OPTION);
						KeywordFilter keywordFilter = new KeywordFilter(keyword);

						elements = keywordFilter.filter(elements);
					}

					Collections.sort(elements, Collections.reverseOrder(new BeerDateComparator()));
					printResults(elements);

					//Write out a log if logging enabled
					if (commandLine.hasOption(AppConfig.LOGGING_OPTION)) {
						Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
						logResult(gson.toJson(elements));
					}
				}
				else {
					System.err.println("No data could be found in response.");
					System.exit(-2);
				}

			}
			else {
				System.err.println("Invalid arguments provided");
				new HelpFormatter().printHelp("Harpoon Brewery", AppConfig.options);
				System.exit(-1);
			}
		}
		catch (ParseException e) {
			System.err.println("Failed to parse arguments. " + e.getMessage());
		}
	}

	private static void printResults(final List<JsonObject> elements) {
		System.out.println("------- Results -------");
		StringBuilder sBuilder = new StringBuilder();

		for (JsonObject object : elements) {
			String name = BeerUtils.getAsString(object, "name");
			String id = BeerUtils.getAsString(object, "id");
			String description = BeerUtils.getAsString(object, "description");
			String dateStr = BeerUtils.getAsString(object, "createDate");
			//special cases, so no attribute is passed in
			Float abv = BeerUtils.getABV(object);
			Float ibu = BeerUtils.getIBU(object);

			DateTime dateTime = dateParsers.parseDateTime(dateStr);
			dateStr = dateTime.toString(dateFormat);


			sBuilder.append("Name: ").append(name).append("\n")
			        .append("ID: ").append(id).append("\n")
			        .append("Description: ").append(description).append("\n")
			        .append("ABV: ").append(abv).append("\n")
			        .append("IBU: ").append(ibu).append("\n")
			        .append("Create Date: ").append(dateStr).append("\n\n");

			System.out.println(sBuilder.toString());
			sBuilder.setLength(0); //reset and reuse the same object
		}
	}

	/**
	 * Write result to a log file with timestamp
	 *
	 * @param s
	 */
	private static void logResult(String s) {
		Date now = new Date();
		String filename = "./resources/out/out-" + now.toString() + ".log";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));
			writer.write(s);
			writer.flush();
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Will check that the command line contains the required arguments to run this program.
	 *
	 * @param commandLine
	 * @return True if all arguments are found on the command line, false otherwise.
	 */
	private static boolean hasRequiredArgs(final CommandLine commandLine) {
		return (commandLine.hasOption(AppConfig.MAX_ABV) || commandLine.hasOption(AppConfig.MIN_ABV));
	}

}
