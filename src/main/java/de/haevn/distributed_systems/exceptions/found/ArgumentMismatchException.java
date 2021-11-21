package de.haevn.distributed_systems.exceptions.found;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "At least one required argument was empty")
public class ArgumentMismatchException extends RuntimeException{ }