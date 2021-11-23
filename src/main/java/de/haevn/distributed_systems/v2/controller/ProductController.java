package de.haevn.distributed_systems.v2.controller;

import de.haevn.distributed_systems.v2.exceptions.*;
import de.haevn.distributed_systems.v2.model.Product;
import de.haevn.distributed_systems.v2.service.ProductService;
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
@RequestMapping(value = "/api/v2/products", produces = "application/json")
public class ProductController implements IController<Product> {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private ProductService service;

    @PostMapping
    @Override
    public ResponseEntity<Product> post(@RequestBody Optional<Product> obj) throws APIException {
        if(obj.isEmpty()){
            throw new ArgumentMismatchException();
        }

        obj.get().setId(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));

        // Tries to save the object, this operation fails when the user already exists
        if(service.save(obj.get())) {
                return AppUtils.generateResponse(HttpStatus.OK, obj.get());
        }
        logger.error("Provided object ({}) generated an internal server error.", obj);
        throw new APIException();
    }

    @GetMapping
    @Override
    public ResponseEntity<List<Product>> get() {
        return AppUtils.generateResponse(HttpStatus.OK, service.findAll());
    }

    @PutMapping
    @Override
    public ResponseEntity<?> put(@RequestBody Optional<ArrayList<Product>> objs) throws APIException {
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
        service.delete();
    }


    @PostMapping("/{id}")
    @Override
    public ResponseEntity<String> postById(@PathVariable  long id) throws APIException {
        throw new ForbiddenException();
    }

    @Override
    public ResponseEntity<Product> getById(long id) {
        var result = service.findById(id);
        if(result.isEmpty()){
            throw new NoObjectExistsException();
        }
        return AppUtils.generateResponse(HttpStatus.OK, result.get());
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Product> putById(long id, Optional<Product> obj) throws APIException {
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
    public void deleteById(long id) {
        service.delete(id);
    }
}
