package de.haevn.distributed_systems.controller;

import de.haevn.distributed_systems.exceptions.ForbiddenException;
import de.haevn.distributed_systems.exceptions.found.NoSuchProductException;
import de.haevn.distributed_systems.exceptions.found.SecurityViolationException;
import de.haevn.distributed_systems.model.Product;
import de.haevn.distributed_systems.service.ProductService;
import de.haevn.distributed_systems.utils.CustomOptionalUtils;
import de.haevn.distributed_systems.utils.SequenceGeneratorService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @SneakyThrows
    @PostMapping(produces = "application/json")
    public ResponseEntity<Product> post(@RequestParam Optional<String> name,
                                       @RequestParam Optional<String> brand,
                                       @RequestParam Optional<String> description,
                                       @RequestParam Optional<String> price){
        CustomOptionalUtils.containsEmptyOptional(name, brand, description, price);
        logger.info("Create new Product");

        Product product = Product.builder()
                .brand(brand.get())
                .name(name.get())
                .description(description.get())
                .newPrice(Double.valueOf(price.get()))
                .oldPrice(Double.valueOf(price.get()))
                .id(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME))
                .build();

        logger.info("Save to database");
        service.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Product>> get(){
        logger.info("Request a list of all products");
        return ResponseEntity.ok(service.findAll());
    }

    @SneakyThrows
    @PutMapping(produces = "application/json")
    public ResponseEntity<String> put(@RequestBody Optional<ArrayList<Product>> objs){
        CustomOptionalUtils.containsEmptyOptional(objs);
        logger.info("Update products");
        long updates = service.update(objs.orElse(new ArrayList<>()));
        return ResponseEntity.status(HttpStatus.OK).body("{\"Updated products\":\"" + updates + "\"}");
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<String> deleteAll(){
        logger.warn("Delete all products invoked");
        service.deleteAll();
        sequenceGeneratorService.resetSequence(Product.SEQUENCE_NAME);
        return ResponseEntity.ok().build();
    }



    @PostMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> postById(@PathVariable long id){
        logger.error("Invoke illegal post operation");
        throw new ForbiddenException();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Product> getById(@PathVariable long id){
        logger.info("Request product object with id {}", id);
        Product product = service.findById(id).orElseThrow(NoSuchProductException::new);
        return ResponseEntity.ok(product);
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<Product> putById(@PathVariable long id, @RequestBody Optional<Product> obj){
        CustomOptionalUtils.containsEmptyOptional(obj);
        Product target = obj.orElse(Product.builder().build());
        Optional<Product> searchResult = service.findById(target.getId());

        String className = target.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        logger.info("Update {} with id {}", className, id);

        if(searchResult.isEmpty()){
            logger.warn("No {} found with id {}", className, id);
            throw new NoSuchProductException();
        }else if(searchResult.get().getId() != id){
            logger.error("Security violation, given id {} does not match the provided {} id {}", id, className, obj.get().getId());
            throw new SecurityViolationException();
        }else if(target.getId() != id){
            logger.error("ID violation. given id {} does not match body id {}", id, target.getId());
            throw new SecurityViolationException();
        }

        logger.info("{} updated", className);
        service.update(target);
        return ResponseEntity.status(HttpStatus.OK).body(target);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> deleteById(@PathVariable long id){
        logger.warn("Delete product {} invoked", id);
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
