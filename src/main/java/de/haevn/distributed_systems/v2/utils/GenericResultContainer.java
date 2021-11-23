package de.haevn.distributed_systems.v2.utils;

import lombok.Data;

@Data
public class GenericResultContainer <T>{
    private String message;
    private T data;
}
