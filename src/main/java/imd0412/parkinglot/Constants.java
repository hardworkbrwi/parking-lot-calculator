package imd0412.parkinglot;

import java.time.format.DateTimeFormatter;

public class Constants {
	private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm";

	public static final DateTimeFormatter DATE_FORMATTER;
	
	static {
		DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
	}
}
