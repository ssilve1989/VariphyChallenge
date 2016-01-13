package com.variphy.challenge;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Ordering;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for actual runs of the application and/or on data produces by actual runs
 */
public class AppTest {

	JsonParser jsonParser;
	static final SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");

	@Before
	public void initialize(){
		jsonParser = new JsonParser();
	}

	//Some nifty Java 8 Streaming happening here to get most recent file
	@Test
	public void checkOrder() throws IOException {
		Path dir = Paths.get("./resources/out/");

		Optional<Path> lastFilePath = Files.list(dir)
		                                   .filter(f -> !Files.isDirectory(f))
		                                   .max((f1, f2) -> (int)(f1.toFile().lastModified() - f2.toFile().lastModified()));

		if (lastFilePath.isPresent()) {
			String rawJson = Files.lines(lastFilePath.get())
					.parallel()
					.map(String::trim)
					.collect(Collectors.joining());

			JsonArray data = jsonParser.parse(rawJson).getAsJsonArray();

			List<Date> createdDates = new ArrayList<>();

			for(JsonElement e : data){
				JsonObject o = e.getAsJsonObject();
				try {
					createdDates.add(format.parse(o.get("createDate").getAsString()));
				}
				catch (ParseException ignored) {}
			}

			Ordering.natural().reverse().isOrdered((createdDates));
		}
	}
}
