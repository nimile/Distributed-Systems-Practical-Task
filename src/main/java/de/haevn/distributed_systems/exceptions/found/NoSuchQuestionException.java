package de.haevn.distributed_systems.exceptions.found;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Question does not exist")
public class NoSuchQuestionException extends RuntimeException{ }