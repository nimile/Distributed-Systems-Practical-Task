package de.haevn.distributed_systems;

import org.slf4j.LoggerFactory;

public class Constants {
    private Constants(){}
    public static final String httpStatusTypeFormatMessage = "Returned status code (%s) does not match required one (%s)";
    public static final String exceptionShouldOccur = "An exception was expected but not thrown";
    public static final String exceptionShouldNotOccur = "No exception was expected but one was thrown";

    public static void logStart(Class<?> target){
        LoggerFactory.getLogger(target).info("Start logging");
    }

    public static void logEnd(Class<?> target){
        LoggerFactory.getLogger(target).info("Stopped logging");
    }
}
