package de.haevn.distributed_systems.utils;

public class CustomStringUtils {
    private CustomStringUtils(){}

    public static boolean isStringNullOrEmpty(String in){
        return null == in || in.isEmpty() || in.isBlank();
    }
}
