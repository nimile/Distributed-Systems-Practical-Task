package de.haevn.distributed_systems.v2.exceptions;

import de.haevn.distributed_systems.v2.exceptions.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Security violation occurred during request")
public class SecurityViolationException extends APIException { }