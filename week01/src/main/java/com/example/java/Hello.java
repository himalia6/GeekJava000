package com.example.java;

public class Hello {
    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int c = a + b;
        int d = c / b;
        int e = c % b;
        int f = c * b;
        int g = 7;
        int h = 128;
        int i = 65536;
        if (g > f) {
            h = h * 2;
        } else {
            h += 20;
        }

        for (int j = 0; j < 5; j++) {
            System.out.println("j = " + j);
        }
        System.out.println("a = " + (a + b));
    }
}
