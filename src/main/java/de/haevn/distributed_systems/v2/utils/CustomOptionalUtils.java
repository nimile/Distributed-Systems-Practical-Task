package de.haevn.distributed_systems.v2.utils;

import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class CustomOptionalUtils {
    public static final Logger logger = LoggerFactory.getLogger(CustomOptionalUtils.class);

    private CustomOptionalUtils(){}



    public static void containsEmptyOptional(Optional<?>... objects) throws ArgumentMismatchException {
        long empties = Arrays.stream(objects).filter(Optional::isEmpty).count();
        logger.info("Checked {} object for null result: {} are empty", objects.length, empties);
        if(empties != 0)throw new ArgumentMismatchException();
    }

    public static void containsNullValue(Object ... objects) throws ArgumentMismatchException {
        long empties = Arrays.stream(objects).filter(Objects::isNull).count();
        logger.info("Checked {} object for null result: {} are null", objects.length, empties);
        if(empties != 0)throw new ArgumentMismatchException();
    }
}
