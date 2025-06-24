package com.bonrix.common.test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ENGTest {

	public static void main(String[] args) {// Input hex string in big-endian format (e.g., "00 00 00 41")
        String hexInput = "00 00 00 41"; // You can change this to any 4-byte hex input

        // Step 1: Split the hex string into individual byte strings
        String[] hexParts = hexInput.trim().split("\\s+");

        // Step 2: Convert each hex string to a byte
        byte[] bytes = new byte[hexParts.length];
        for (int i = 0; i < hexParts.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexParts[i], 16);
        }

        // Step 3: Reverse the byte array to convert from big-endian to little-endian
        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - 1 - i];
            bytes[bytes.length - 1 - i] = temp;
        }

        // Step 4: Wrap the byte array into a ByteBuffer and interpret as float
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN); // Explicitly set to little-endian
        float result = bb.getFloat();

        // Step 5: Print the result
        System.out.println("Converted Float Value: " + result);}

	static String hexToBin(String s) {
		  return new BigInteger(s, 16).toString(2);
		}
	
	public static String reverse(String str)
	{
StringBuilder sb = new StringBuilder();
        for(int i = str.length() - 1; i >= 0; i--)
            sb.append(str.charAt(i));
        return sb.toString();
	}
	
}
