package com.bonrix.common.test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LittleEndianToFloat {
    public static void main(String[] args) {
        // Step 1: Define the byte sequence in little-endian order
        byte[] bytes = new byte[]{ 0x00, 0x00, 0x10,0x41,}; // Reversed from the big-endian order (00 00 00 41)

        // Step 2: Wrap the byte array into a ByteBuffer and set it to little-endian
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Set byte order to little-endian

        // Step 3: Convert the bytes to a float
        float floatValue = buffer.getFloat();

        // Step 4: Output the result
        System.out.println("The 32-bit floating-point value is: " + floatValue);
    }
}
