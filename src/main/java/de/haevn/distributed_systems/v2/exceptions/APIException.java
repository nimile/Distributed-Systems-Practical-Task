package de.haevn.distributed_systems.v2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Something went terrible wrong")
public class APIException extends  RuntimeException{}
