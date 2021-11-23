package de.haevn.distributed_systems.v2.utils;

import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class AppUtils {
    private AppUtils(){}


    public static void containsEmptyOptional(Optional<?>... objects) throws ArgumentMismatchException {
        long empties = Arrays.stream(objects).filter(Optional::isEmpty).count();
        if(empties != 0)throw new ArgumentMismatchException();
    }

    public static void containsNullValue(Object ... objects) throws ArgumentMismatchException {
        long empties = Arrays.stream(objects).filter(Objects::isNull).count();
        if(empties != 0)throw new ArgumentMismatchException();
    }

    public static boolean isStringNullOrEmpty(String in){
        return null == in || in.isEmpty() || in.isBlank();
    }

    public static boolean isStringNeitherNullNorEmpty(String in){
        return !isStringNullOrEmpty(in);
    }


    public static <T> ResponseEntity<T> generateResponse(HttpStatus status, T body){
        return ResponseEntity.status(status).body(body);
    }
}
