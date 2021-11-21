package de.haevn.distributed_systems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Requested argument conflicts")
public class ConflictException extends RuntimeException{

    public ConflictException(){
        super();
    }
}
