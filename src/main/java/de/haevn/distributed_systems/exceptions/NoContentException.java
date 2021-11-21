package de.haevn.distributed_systems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No content was provided")
public class NoContentException extends RuntimeException{ }
