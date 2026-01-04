package com.substring.foodies.Utility;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Helper {

    public static String generateRandomId(){
        return UUID.randomUUID().toString();
    }
    public static int generateIntRandomId(){
        return UUID.randomUUID().hashCode();
    }

    public static String normalize(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

}
