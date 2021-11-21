package de.haevn.distributed_systems.exceptions.found;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Security violation occurred during request")
public class SecurityViolationException extends RuntimeException{ }