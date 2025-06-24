package com.bonrix.common.test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HexToFloatConverter {

    public static void main(String[] args) {
        // Step 1: Input hexadecimal string 4140
      //  String inputHex = "40A0";
    	String inputHex = "4140";
        // Step 2: Convert the hex string to a byte array
        byte[] originalBytes = hexStringToByteArray(inputHex);

        // Step 3: Swap the bytes (Little-endian assumption)
        byte[] swappedBytes = new byte[]{originalBytes[1], originalBytes[0]};

        // Step 4: Prepend two zero bytes to form a 4-byte array (00 00 10 41)
        byte[] finalBytes = new byte[]{
                (byte) 0x00,
                (byte) 0x00,
                swappedBytes[0],
                swappedBytes[1]
        };

        // Step 5: Wrap the byte array into a ByteBuffer and set byte order to little-endian
        ByteBuffer buffer = ByteBuffer.wrap(finalBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // Step 6: Convert the bytes to a float
        float floatValue = buffer.getFloat();

        // Step 7: Output the result
        System.out.println("The 32-bit floating-point value is: " + floatValue);
    }

    /**
     * Converts a hexadecimal string to a byte array.
     *
     * @param hex the input hexadecimal string (must have even length)
     * @return the corresponding byte array
     */
    public static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length.");
        }

        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}