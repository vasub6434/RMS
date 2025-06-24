package com.bonrix.common.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.bonrix.dggenraterset.TcpServer.EnergyMeterServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Test {
	 static Logger log = Logger.getLogger(EnergyMeterServer.class);
	 private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
	
	public static void main(String[] args) throws ParseException, JsonProcessingException {
		
		//System.out.println(LocalTime.MIN.plusSeconds(1400).toString());
		int totalMinutes=1400;
		// Convert total minutes into total seconds
        int totalSeconds = totalMinutes * 60;

        // Calculate hours, minutes, and seconds
        int hours = totalSeconds / 3600;
        int remainingSeconds = totalSeconds % 3600;
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        System.out.println(hours);
        // Print the result
        System.out.printf("%d minutes is equivalent to: %d hour(s), %d minute(s), %d second(s)%n", totalMinutes, hours, minutes, seconds);
	}
	
	 static String hexToBinary(String hex)
	    {
	 
	        // variable to store the converted
	        // Binary Sequence
	        String binary = "";
	 
	        // converting the accepted Hexadecimal
	        // string to upper case
	        hex = hex.toUpperCase();
	 
	        // initializing the HashMap class
	        HashMap<Character, String> hashMap
	            = new HashMap<Character, String>();
	 
	        // storing the key value pairs
	        hashMap.put('0', "0000");
	        hashMap.put('1', "0001");
	        hashMap.put('2', "0010");
	        hashMap.put('3', "0011");
	        hashMap.put('4', "0100");
	        hashMap.put('5', "0101");
	        hashMap.put('6', "0110");
	        hashMap.put('7', "0111");
	        hashMap.put('8', "1000");
	        hashMap.put('9', "1001");
	        hashMap.put('A', "1010");
	        hashMap.put('B', "1011");
	        hashMap.put('C', "1100");
	        hashMap.put('D', "1101");
	        hashMap.put('E', "1110");
	        hashMap.put('F', "1111");
	 
	        int i;
	        char ch;
	 
	        // loop to iterate through the length
	        // of the Hexadecimal String
	        for (i = 0; i < hex.length(); i++) {
	            // extracting each character
	            ch = hex.charAt(i);
	 
	            // checking if the character is
	            // present in the keys
	            if (hashMap.containsKey(ch))
	 
	                // adding to the Binary Sequence
	                // the corresponding value of
	                // the key
	                binary += hashMap.get(ch);
	 
	            // returning Invalid Hexadecimal
	            // String if the character is
	            // not present in the keys
	            else {
	                binary = "Invalid Hexadecimal String";
	                return binary;
	            }
	        }
	 
	        // returning the converted Binary
	        return binary;
	    }
	 
	private static String hexToBin(String hex){
		String digits = "0123456789ABCDEF";
  		hex = hex.toUpperCase();
		String binaryString = "";
		
		for(int i = 0; i < hex.length(); i++) {
			char c = hex.charAt(i);
			int d = digits.indexOf(c);
			if(d == 0)	binaryString += "0000"; 
			else  binaryString += Integer.toBinaryString(d);
		}
		return new BigInteger(hex, 16).toString(2)=="0"?"00000000":new BigInteger(hex, 16).toString(2);
    }
	
	static String ConvertMODBUSRTUValue(String hexVal)
	{
        Long i1 = Long.parseLong(hexVal, 16);
        Float f1 = Float.intBitsToFloat(i1.intValue());
      //  System.out.println(String.format("%d", f1));
        return String.valueOf(f1);
	}
}
