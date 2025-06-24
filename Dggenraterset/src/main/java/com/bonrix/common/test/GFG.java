package com.bonrix.common.test;
// Java program to convert hexadecimal to decimal
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GFG { public static int hexToDecimal(String hexnum){  
	String hstring = "0123456789ABCDEF";  
	hexnum = hexnum.toUpperCase();  
	int num = 0;  
	for (int i = 0; i < hexnum.length(); i++)  
	{  
		char ch = hexnum.charAt(i);  
		int n = hstring.indexOf(ch);  
		num = 16*num + n;  
	}  
	return num;  
   }  

public static String convertHexToString(String hex) {

	StringBuilder sb = new StringBuilder();
	StringBuilder temp = new StringBuilder();

	// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
	for (int i = 0; i < hex.length() - 1; i += 2) {

		// grab the hex in pairs
		String output = hex.substring(i, (i + 2));
		// convert hex to decimal
		int decimal = Integer.parseInt(output, 16);
		// convert the decimal to character
		sb.append((char) decimal);

		temp.append(decimal);
	}
	//log.info("Decimal : " + temp.toString());

	return sb.toString();
}
   public static void main(String args[]){    
	   Pattern pattern = Pattern.compile("(\\d+) Days: (\\d+) Hour: (\\d+) Minutes: (\\d+) Seconds");
       Matcher matcher = pattern.matcher("0 Days: 23 Hour: 59 Minutes: 46 Seconds");

       if (matcher.find()) {
           String days = matcher.group(1);
           String hours = matcher.group(2);
           String minutes = matcher.group(3);
           String seconds = matcher.group(4);

           System.out.println("Days: " + days);
           System.out.println("Hours: " + hours);
           System.out.println("Minutes: " + minutes);
           System.out.println("Seconds: " + seconds);
       } else {
           System.out.println("No match found.");
       }  
   }

private static char[] BigInteger(String string, int i) {
	// TODO Auto-generated method stub
	return null;
}
   }
