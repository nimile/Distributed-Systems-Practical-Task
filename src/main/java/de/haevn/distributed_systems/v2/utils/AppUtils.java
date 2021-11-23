package de.haevn.distributed_systems.v2.utils;

import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class AppUtils {
    private AppUtils(){}


    /**
     * Checks if a list of optionals contains an empty one.<br>
     * If at least one empty optional was found an {@link ArgumentMismatchException} is thrown.
     * @param objects Optionals to validated
     * @throws ArgumentMismatchException Thrown iff an empty optional was found
     */
    public static void containsEmptyOptional(Optional<?>... objects) throws ArgumentMismatchException {
        long empties = Arrays.stream(objects).filter(Optional::isEmpty).count();
        if(empties != 0)throw new ArgumentMismatchException();
    }

    /**
     * Checks if a list contains a null value.<br>
     * If at least one null object was found an {@link ArgumentMismatchException} is thrown.
     * @param objects Objects to validated
     * @throws ArgumentMismatchException Thrown iff null object was found
     */
    public static void containsNullValue(Object ... objects) throws ArgumentMismatchException {
        long empties = Arrays.stream(objects).filter(Objects::isNull).count();
        if(empties != 0)throw new ArgumentMismatchException();
    }

    /**
     * Checks if a string is null or empty.
     * @param in String to validate
     * @return true iff the string is null or empty
     */
    public static boolean isStringNullOrEmpty(String in){
        return null == in || in.isEmpty() || in.isBlank();
    }

    /**
     * Checks if a string is neither null nor empty.
     * @param in String to validate
     * @return true iff the string is neither null nor empty
     */
    public static boolean isStringNeitherNullNorEmpty(String in){
        return !isStringNullOrEmpty(in);
    }

    /**
     * Generates a {@link ResponseEntity}. <br>
     * This generic method takes a result and an object to assemble a result.
     * @param status Http code
     * @param body Body of the entity
     * @param <T> Body object
     * @return {@link ResponseEntity}
     */
    public static <T> ResponseEntity<T> generateResponse(HttpStatus status, T body){
        return ResponseEntity.status(status).body(body);
    }
}
