package de.haevn.distributed_systems.v2.exceptions;

import de.haevn.distributed_systems.v2.exceptions.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested object does not exist")
public class NoObjectExistsException extends APIException { }