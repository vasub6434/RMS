package com.bonrix.common.test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Main {
    public static void main(String[] args) {
        int number = 16640;//16640;

        // Allocate a ByteBuffer with a size of 4 bytes (for int)
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);  // Set the byte order to little-endian

        // Put the integer into the buffer
        buffer.putInt(number);

        // Get the byte array
        byte[] bytes = buffer.array();

        // Print the byte array in hexadecimal
        System.out.print("Hexadecimal representation: ");
        for (byte b : bytes) {
            System.out.printf("%02X ", b);
        }
        
        
        
    }
}
