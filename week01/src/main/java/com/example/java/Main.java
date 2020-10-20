package com.example.java;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        HelloClassLoader classLoader = new HelloClassLoader();
        try {
            Class<?> hello = classLoader.loadClass("Hello");
            Method method = hello.getDeclaredMethod("hello");
            method.invoke(hello.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
