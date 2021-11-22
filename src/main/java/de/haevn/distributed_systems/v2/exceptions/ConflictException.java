package de.haevn.distributed_systems.v2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Requested contains one or more conflicts")
public class ConflictException extends APIException{}
