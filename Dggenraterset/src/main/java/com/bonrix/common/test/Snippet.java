package com.bonrix.common.test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
public class Snippet {
	 public static void main(String[] args) {
	        // Sample 32-bit float
	        float originalFloat = 16608.00f;
	

	        // Convert the float to bytes in little-endian format
	        ByteBuffer buffer = ByteBuffer.allocate(4);
	        buffer.order(ByteOrder.LITTLE_ENDIAN);
	        buffer.putFloat(originalFloat);

	        byte[] littleEndianBytes = buffer.array();

	        // Print original float and little-endian byte representation
	        System.out.println("Original float: " + originalFloat);
	        System.out.print("Little-endian byte representation: ");
	        for (byte b : littleEndianBytes) {
	            System.out.printf("0x%02X ", b);
	        }
	        System.out.println();

	        // Swap the byte order (Little-endian to Big-endian or vice versa)
	        byte[] swappedBytes = new byte[4];
	        for (int i = 0; i < littleEndianBytes.length; i++) {
	            swappedBytes[i] = littleEndianBytes[littleEndianBytes.length - 1 - i];
	        }

	        // Print swapped bytes
	        System.out.print("Swapped byte representation: ");
	        for (byte b : swappedBytes) {
	            System.out.printf("0x%02X ", b);
	        }
	        System.out.println();

	        // Convert swapped bytes back to float (interpreting as Big-endian)
	        ByteBuffer swappedBuffer = ByteBuffer.wrap(swappedBytes);
	        swappedBuffer.order(ByteOrder.BIG_ENDIAN);  // Set to BIG_ENDIAN to match the byte swap
	        float swappedFloat = swappedBuffer.getFloat();

	        // Print swapped float
	        System.out.println("Swapped float: " + swappedFloat);
	    }
}

