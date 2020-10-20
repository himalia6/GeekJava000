package com.example.java;

import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.equals("Hello")) {
            byte[] bytes = new byte[0];
            try {
                bytes = Files.readAllBytes(Paths.get(getResource("Hello.xlass").toURI()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }

            return defineClass("Hello", bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }
}
