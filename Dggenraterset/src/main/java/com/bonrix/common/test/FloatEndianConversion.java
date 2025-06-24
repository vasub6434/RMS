package com.bonrix.common.test;
public class FloatEndianConversion {
    public static void main(String[] args) {
        float originalFloat =Float.parseFloat("16544");
        
        // Convert the float to a byte array (using Float.floatToIntBits)
        int originalBits = Float.floatToIntBits(originalFloat);
        
        // Convert to big endian (no change needed as int is already in big endian on most systems)
        int bigEndian = originalBits;
        
        // Swap endianess to little endian
        int littleEndian = swapEndian(bigEndian);
        
        // Print the results
        System.out.printf("Original Float: %f\n", originalFloat);
        System.out.printf("Big Endian (Hex): 0x%X\n", bigEndian);
        System.out.printf("Little Endian (Hex): 0x%X\n", littleEndian);
    }
    // Method to swap the endian of a 32-bit integer
    public static int swapEndian(int value) {
        return Integer.reverseBytes(value);
    }
}
