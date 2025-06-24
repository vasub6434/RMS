package com.bonrix.common.test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FloatByteSwap {
    public static void main(String[] args) {
        // Modified 32-bit float
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

        // Swap the byte order (Big-endian to Little-endian or vice versa)
        byte[] swappedBytes = new byte[4];
        for (int i = 0; i < littleEndianBytes.length; i++) {
            swappedBytes[i] = littleEndianBytes[littleEndianBytes.length - 1 - i];
        }

        // Print swapped bytes
        System.out.print("\nSwapped byte representation: ");
        for (byte b : swappedBytes) {
            System.out.printf("0x%02X ", b);
        }

        // Convert the swapped bytes back to float
        ByteBuffer swappedBuffer = ByteBuffer.wrap(swappedBytes);
        swappedBuffer.order(ByteOrder.LITTLE_ENDIAN); // Set to Little-endian for the float conversion
        float swappedFloat = swappedBuffer.getFloat();

        // Print the swapped float value
        System.out.println("\nSwapped float value: " + swappedFloat);
    }
}
