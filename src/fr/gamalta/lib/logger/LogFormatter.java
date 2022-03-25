package fr.gamalta.lib.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

	private DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.FRANCE);

	@Override
	public String format(LogRecord paramLogRecord) {

		return getDateAndTime() + " " + paramLogRecord.getMessage() + "\n";
	}

	private String getDateAndTime() {

		Date date = new Date();
		return dateFormat.format(date);
	}
}