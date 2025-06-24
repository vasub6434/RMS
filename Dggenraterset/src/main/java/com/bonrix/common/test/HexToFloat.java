package com.bonrix.common.test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HexToFloat {
    public static void main(String[] args) {
        // Hex bytes: 00 00 00 41 (Little-endian)
        byte[] bytes = new byte[] { 0x00, 0x00, 0x00, 0x41 };

        // Wrap in ByteBuffer and set to little-endian
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Important for little-endian format

        // Read as float
        float value = buffer.getFloat();

        System.out.println("Float value: " + value);
    }
}