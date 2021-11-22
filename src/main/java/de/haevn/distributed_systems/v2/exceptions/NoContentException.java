package de.haevn.distributed_systems.v2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No content was provided")
public class NoContentException extends APIException{ }
