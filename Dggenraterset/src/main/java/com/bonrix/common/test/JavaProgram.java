package com.bonrix.common.test;
import java.util.Scanner;

public class JavaProgram
{
	private static String parseHexBinary(String hex) {
		String digits = "0123456789ABCDEF";
  		hex = hex.toUpperCase();
		String binaryString = "";
		
		for(int i = 0; i < hex.length(); i++) {
			char c = hex.charAt(i);
			int d = digits.indexOf(c);
			if(d == 0)	binaryString += "0000"; 
			else  binaryString += Integer.toBinaryString(d);
		}
		return binaryString;
	}
	
    public static void main(String args[])
    {
    	
    	System.out.println(parseHexBinary("2A5B"));
    }
}