package de.haevn.distributed_systems.v2.controller;


import de.haevn.distributed_systems.v2.exceptions.APIException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IController<T> {

    ResponseEntity<T> post(@RequestBody Optional<T> obj) throws APIException;
    ResponseEntity<List<T>> get();
    ResponseEntity<?> put(@RequestBody Optional<ArrayList<T>> objs) throws APIException;
    void delete();

    ResponseEntity<String> postById(@PathVariable long id) throws APIException;
    ResponseEntity<T> getById(@PathVariable long id);
    ResponseEntity<T> putById(@PathVariable long id, @RequestBody Optional<T> obj) throws APIException;
    void deleteById(@PathVariable long id);
}
