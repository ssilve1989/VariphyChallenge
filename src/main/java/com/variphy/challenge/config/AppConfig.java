package com.variphy.challenge.config;

import org.apache.commons.cli.Options;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

public class AppConfig {

	//Command Line Options
	public static final String KEYWORD_OPTION = "keyword";
	public static final String MIN_ABV = "abvMin";
	public static final String MAX_ABV = "abvMax";
	public static final String LOGGING_OPTION = "loggingEnabled";

	public static final Options options = new Options();
	static {
		options.addOption(KEYWORD_OPTION, true, "Search keyword");
		options.addOption(MIN_ABV, true, "Minimum ABV (Alcohol By Volume) value");
		options.addOption(MAX_ABV, true, "Maximum ABV (Alcohol By Volume) value");
		options.addOption(LOGGING_OPTION, false, "Enable logging of an output file");
	}

	public static final DateTimeFormatter dateParsers;
	static{
		DateTimeParser[] parsers = {
				DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").getParser(),
				DateTimeFormat.forPattern("yy-MM-dd HH:mm:ss").getParser(),
		};
		dateParsers = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();
	}

	//API Info
	public static final String BREWERY_ID = "RzvedX";
	public static final String API_KEY = "6495f3ff13746e6935cd6e148f69c030";
}
