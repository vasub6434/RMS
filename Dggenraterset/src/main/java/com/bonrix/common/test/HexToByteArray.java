package com.bonrix.common.test;
public class HexToByteArray {
    public static void main(String[] args) {
        // Hex string
      //  String hexString = "00 00 00 41";
        String hexString = "A0 40 00 00";

        // Remove spaces and convert to byte array
        String[] hexArray = hexString.split(" ");
        byte[] byteArray = new byte[hexArray.length];

        for (int i = 0; i < hexArray.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(hexArray[i], 16);
        }

        // Print the byte array
        System.out.print("byte[] = { ");
        for (byte b : byteArray) {
            System.out.printf("0x%02X, ", b);
        }
        System.out.println("}");
    }
}
