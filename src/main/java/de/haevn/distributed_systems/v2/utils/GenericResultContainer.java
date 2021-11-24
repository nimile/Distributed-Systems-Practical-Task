package de.haevn.distributed_systems.v2.utils;

import lombok.Data;

/**
 * This container class can be used as a single result object.<br>
 * It contains a message and a generic data object<br>
 * @param <T> Object type
 */
@Data
public class GenericResultContainer <T>{
    private String message;
    private T data;
}
