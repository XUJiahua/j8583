package com.solab.iso8583.cardinfolink;

public class TestOnly {
    public static void main(String[] args){
        String x = String.format("%04X", 74);
        String y = String.format("%04X", 256);
    }

    private static void TestBitmap() {
        // bitmap is fine
        int bits = 64;
        long num = 0x0000000000C00010;
        for (int i = 0; i < bits; i++) {
            if ((num & 0b1) == 1) {
                System.out.println((bits - i) + ":" + (num & 0b1));
            }
            num = num >> 1;
        }
    }
}