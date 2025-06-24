/**************************************************************************************************
 * File Name : MessageInfoCache.java
 * @author   : SAMIR 
 * Version Information : 1.0
 * Reads the Application Messages properties file
 * Modification History:
 * Date		 Modified By		Description 
 *27-02-2018  SAMIR			Reads the Application Messages properties file
 ************************************************************************************************* */
package com.bonrix.common.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Class defines method to read the Application Messages properties file and
 * store in a singleton instance
 * 
 * @author SAMIR
 */
public class MessageInfoCache {
	static final Logger logger = Logger.getLogger(MessageInfoCache.class);
	private MessageInfoCache() {
	}

	private static MessageInfoCache instance = null;
	private static Properties properties = null;

	public static MessageInfoCache getInstance() {
		/**
		 * Creates new instance if not created and reads the properties file
		 */
		if (instance == null) {
			instance = new MessageInfoCache();
			properties = new MessageInfoCache()
					.getProperty("applicationMessages.properties");
		}
		return instance;
	}

	public Properties getProperty(String propertyFileName) {
		Properties properties = new Properties();
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propertyFileName);

		if (inputStream != null) {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
			}

		} else {
			try {
				throw new FileNotFoundException("property file '"
						+ propertyFileName + "' not found in the classpath");
			} catch (FileNotFoundException e) {
			}
		}
		return properties;
	}

	/**
	 * Gets the message string corresponding to the given message code
	 * 
	 * @param messageCode
	 * @return
	 */
	public static String getMessageString(String messageCode) {
		getInstance();
		return properties.getProperty(messageCode)!=null? properties.getProperty(messageCode):"";
	}
	
	
	public static String applyMessageTemplate(String pattern, String messageCode){
		getInstance();
		return properties.getProperty(messageCode).replace("$req_param$", pattern)!=null ?
				properties.getProperty(messageCode).replace("$req_param$", pattern):"";
	}
	
	public static String applyMultipleParamsTemplate(String pattern, String messageCode){
		getInstance();
		  String[] result = pattern.split("\\|");
		  String messageString = properties.getProperty(messageCode);
		  int i=1;
		  for(String s : result){
			 messageString = messageString.replace("req_param_"+i++, s);
	        }
		return messageString!=null? messageString:"";
	}
	
	public void clear() throws Throwable {
		try{
			properties.clear();
			properties = null;
			instance = null;
		}catch(Exception e){
			logger.error("Exception in finalize of MessageInfoCache ",e);
		}
	}
}
