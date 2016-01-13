package com.variphy.challenge.config;

import org.apache.commons.cli.Options;

public class AppConfig {

	//Command Line Options
	public static final String KEYWORD_OPTION = "keyword";
	public static final String MIN_ABV_OPTION = "abvMin";
	public static final String MAX_ABV_OPTION = "abvMax";
	public static final String LOGGING_OPTION = "loggingEnabled";

	public static final Options options = new Options();
	static {
		options.addOption(KEYWORD_OPTION, true, "Search keyword");
		options.addOption(MIN_ABV_OPTION, true, "Minimum ABV (Alcohol By Volume) value");
		options.addOption(MAX_ABV_OPTION, true, "Maximum ABV (Alcohol By Volume) value");
		options.addOption(LOGGING_OPTION, false, "Enable logging of an output file");
	}

	//API Info
	public static final String BREWERY_ID = "RzvedX";
	public static final String API_KEY = "6495f3ff13746e6935cd6e148f69c030";
}
