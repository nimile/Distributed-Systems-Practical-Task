package de.haevn.distributed_systems.v2.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NetworkUtils {
    private NetworkUtils(){}

    public static <T> ResponseEntity<T> generateResponse(HttpStatus status, T body){
        return ResponseEntity.status(status).body(body);
    }
}
