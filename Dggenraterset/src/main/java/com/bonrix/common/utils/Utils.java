package com.bonrix.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Utils {

	private Properties properties;	
	public static Utils instance;
	
	public Utils() {
		this.properties = new Properties();
	}
	
	/**
	 * call this method only in web app listener :: initialized
	 * @return
	 * @throws IOException
	 */
	public static Utils getInstance () throws IOException {
		if (instance == null) {
			instance = new Utils();
			InputStream stream = instance.getClass().getClassLoader()
					.getResourceAsStream("applicationMessages.properties");
			instance.properties.load(stream);
		}
		return instance;
	}
	
	public String getSetting(String key)  {
			
		return this.properties.getProperty(key) != null 
				? this.properties.getProperty(key).trim() : "";
	}
	public String format(Date date, String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	public Date parse(String dateStr, String pattern) throws ParseException {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.parse(dateStr);
	}
	public Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	public Date toDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
	
	public void clear() {
		properties.clear();
		properties = null;
		Utils.instance = null;
	}
}