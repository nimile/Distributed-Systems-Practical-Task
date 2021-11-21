package de.haevn.distributed_systems.controller;


import de.haevn.distributed_systems.exceptions.ForbiddenException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.exceptions.found.NoSuchReviewException;
import de.haevn.distributed_systems.exceptions.found.SecurityViolationException;

import de.haevn.distributed_systems.model.Review;
import de.haevn.distributed_systems.service.ReviewService;
import de.haevn.distributed_systems.utils.CustomOptionalUtils;
import de.haevn.distributed_systems.utils.SequenceGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService service;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<Review> post(@RequestParam Optional<String> publisher


    ) throws ArgumentMismatchException {
        CustomOptionalUtils.containsEmptyOptional(publisher);
        logger.info("Create new Review");

        Review review = Review.builder()
                .id(sequenceGeneratorService.generateSequence(Review.SEQUENCE_NAME))

                .build();

        logger.info("Save to database");
        service.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }





    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Review>> getAll(){
        logger.info("Request a list of all reviews");
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<String> putAll(@RequestBody Optional<ArrayList<Review>> objs) throws ArgumentMismatchException {
        logger.info("Update reviews");
        CustomOptionalUtils.containsEmptyOptional(objs);
        long updates = service.update(objs.orElse(new ArrayList<>()));
        return ResponseEntity.status(HttpStatus.OK).body("{\"Updated products\":\"" + updates + "\"}");
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<String> deleteAll(){
        logger.warn("Delete all reviews invoked");
        service.deleteAll();
        sequenceGeneratorService.resetSequence(Review.SEQUENCE_NAME);
        return ResponseEntity.ok().build();
    }



    @PostMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> postId(@PathVariable Long id){
        logger.error("Invoke illegal post operation");
        throw new ForbiddenException();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Review> getId(@PathVariable Long id){
        logger.info("Request review object with id {}", id);
        Review user = service.findById(id).orElseThrow(NoSuchReviewException::new);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Review> putId(@PathVariable long id, @RequestBody Optional<Review> obj) throws ArgumentMismatchException {
        CustomOptionalUtils.containsEmptyOptional(obj);
        Review target = obj.orElse(Review.builder().build());
        Optional<Review> searchResult = service.findById(target.getId());

        String className = target.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        logger.info("Update {} with id {}", className, id);

        if(searchResult.isEmpty()){
            logger.warn("No {} found with id {}", className, id);
            throw new NoSuchReviewException();
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
    public ResponseEntity<String> deleteId(@PathVariable Long id){
        logger.warn("Delete review {} invoked", id);
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
