package de.haevn.distributed_systems.v2.controller;

import de.haevn.distributed_systems.v2.exceptions.*;
import de.haevn.distributed_systems.v2.model.Order;
import de.haevn.distributed_systems.v2.service.OrderService;
import de.haevn.distributed_systems.v2.utils.AppUtils;
import de.haevn.distributed_systems.v2.utils.GenericResultContainer;
import de.haevn.distributed_systems.v2.utils.sequence_generator.SequenceGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v2/orders", produces = "application/json")
public class OrderController implements IController<Order> {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private OrderService service;

    @PostMapping
    @Override
    public ResponseEntity<Order> post(@RequestBody Optional<Order> obj) throws APIException {
        if(obj.isEmpty() || AppUtils.isStringNullOrEmpty(obj.get().getPayment()) || null == obj.get().getTotal()){
            throw new ArgumentMismatchException();
        }

        obj.get().setId(sequenceGeneratorService.generateSequence(Order.SEQUENCE_NAME));

        // Tries to save the object, this operation fails when the user already exists
        if(service.save(obj.get())) {
            return AppUtils.generateResponse(HttpStatus.OK, obj.get());
        }
        logger.error("Provided object ({}) generated an internal server error.", obj);
        throw new APIException();
    }

    @GetMapping
    @Override
    public ResponseEntity<List<Order>> get() {
        return AppUtils.generateResponse(HttpStatus.OK, service.findAll());
    }

    @PutMapping
    @Override
    public ResponseEntity<?> put(@RequestBody Optional<ArrayList<Order>> objs) throws APIException {
        if(objs.isEmpty()){
            throw new ArgumentMismatchException();
        }
        var updates = service.update(objs.get());
        GenericResultContainer<Long> container = new GenericResultContainer<>();
        container.setMessage("Updated users");
        container.setData(updates);
        return AppUtils.generateResponse(HttpStatus.OK, container);
    }

    @DeleteMapping
    @Override
    public void delete() {
        sequenceGeneratorService.resetSequence(Order.SEQUENCE_NAME);
        service.delete();
    }

    @PostMapping("/{id}")
    @Override
    public ResponseEntity<String> postById(@PathVariable long id) throws APIException {
        throw new ForbiddenException();
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Order> getById(@PathVariable long id) {
        var result = service.findById(id);
        if(result.isEmpty()){
            throw new NoObjectExistsException();
        }
        return AppUtils.generateResponse(HttpStatus.OK, result.get());
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Order> putById(@PathVariable long id, @RequestBody Optional<Order> obj) throws APIException {
        if(obj.isEmpty()){
            throw new ArgumentMismatchException();
        }else if(obj.get().getId() == null || obj.get().getId() != id){
            throw new ConflictException();
        }
        var result = service.update(obj.get());

        // If the operation fails throw a new APIException
        if(result.isEmpty()){
            throw new APIException();
        }
        return AppUtils.generateResponse(HttpStatus.OK, result.get());
    }

    @DeleteMapping("/{id}")
    @Override
    public void deleteById(@PathVariable long id) {
        service.delete(id);
    }
}
