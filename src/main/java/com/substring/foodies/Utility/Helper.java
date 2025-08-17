package com.substring.foodies.Utility;

import java.util.UUID;

public class Helper {

    public static String generateRandomId(){
        return UUID.randomUUID().toString();
    }
    public static int generateIntRandomId(){
        return UUID.randomUUID().hashCode();
    }
}
