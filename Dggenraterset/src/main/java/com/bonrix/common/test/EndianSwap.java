package com.bonrix.common.test;
public class EndianSwap {


	    public static void main(String[] args) {
	        float originalFloat =16608.00f;

	        // Convert the float to bytes in big-endian format
	        int bigEndian = Float.floatToIntBits(originalFloat);

	        // Swap the byte order (big-endian to little-endian or vice versa)
	        int littleEndian = swapEndian32(bigEndian);

	        // Print the original float and its byte representations
	        System.out.println("Original Float: " + originalFloat);
	        System.out.printf("Big Endian (Hex): 0x%X\n", bigEndian);
	        System.out.printf("Little Endian (Hex): 0x%X\n", littleEndian);

	        // Convert the little-endian value back to a decimal float
	        float decimalValueFromLittleEndian = Float.intBitsToFloat(littleEndian);
	        System.out.println("Decimal from Little Endian (Hex): " + decimalValueFromLittleEndian);
	    }

	    // Method to swap the byte order of a 32-bit integer
	    private static int swapEndian32(int value) {
	        return Integer.reverseBytes(value);
	}

}