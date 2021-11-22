package de.haevn.distributed_systems.v2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Requested method is forbidden to access")
public class ForbiddenException extends APIException{ }
