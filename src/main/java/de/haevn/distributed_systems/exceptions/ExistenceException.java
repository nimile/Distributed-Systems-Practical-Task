package de.haevn.distributed_systems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Provided element already exists")
public class ExistenceException extends Exception{ }
