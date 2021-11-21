package de.haevn.distributed_systems.controller;


import de.haevn.distributed_systems.exceptions.ExistenceException;
import de.haevn.distributed_systems.exceptions.ForbiddenException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.exceptions.found.NoSuchQuestionException;
import de.haevn.distributed_systems.exceptions.found.SecurityViolationException;
import de.haevn.distributed_systems.model.Question;
import de.haevn.distributed_systems.service.QuestionService;
import de.haevn.distributed_systems.utils.CustomOptionalUtils;
import de.haevn.distributed_systems.utils.SequenceGeneratorService;
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
@RequestMapping("/questions")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService service;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<Question> post(@RequestBody Optional<Question> question) throws ExistenceException, ArgumentMismatchException {
        var user = service.save(question.orElse(null));
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Question>> get(){
        logger.info("Request a list of all questions");
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<String> put(@RequestBody Optional<ArrayList<Question>> objs) throws ArgumentMismatchException {
        logger.info("Update questions");
        CustomOptionalUtils.containsEmptyOptional(objs);
        long updates = service.update(objs.orElse(new ArrayList<>()));
        return ResponseEntity.status(HttpStatus.OK).body("{\"Updated questions\":\"" + updates + "\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(){
        logger.warn("Delete all users invoked");
        service.deleteAll();
        sequenceGeneratorService.resetSequence(Question.SEQUENCE_NAME);
        return ResponseEntity.ok().build();
    }



    @PostMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> postById(@PathVariable long id){
        logger.error("Invoke illegal post operation");
        throw new ForbiddenException();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Question> getById(@PathVariable long id){
        logger.info("Request question object with id {}", id);
        Question user = service.findById(id).orElseThrow(NoSuchQuestionException::new);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Question> putById(@PathVariable long id, @RequestBody Optional<Question> obj) throws ArgumentMismatchException {
        logger.info("Update user with id {}", id);
        CustomOptionalUtils.containsEmptyOptional(obj);

        // Extract the user from the argument
        var targetQuestion = obj.orElse(Question.builder().build());

        // Lookup the user
        Optional<Question> userByEmail = service.findById(targetQuestion.getId());
        if(userByEmail.isEmpty()){
            logger.warn("No user found with id {}", id);
            throw new NoSuchQuestionException();
        }else if(userByEmail.get().getId() != id){
            logger.error("Security violation, given id {} does not match the provided email address id {}", id, userByEmail.get().getId());
            throw new SecurityViolationException();
        }else if(targetQuestion.getId() != id){
            logger.error("ID violation. given id {} does not match body id {}", id, targetQuestion.getId());
            throw new SecurityViolationException();
        }

        logger.info("question updated");
        service.update(targetQuestion);
        return ResponseEntity.status(HttpStatus.OK).body(targetQuestion);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> deleteById(@PathVariable long id){
        logger.warn("Delete question {} invoked", id);
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }




    @GetMapping(value="/uid/{uid}", produces = "application/json")
    public ResponseEntity<List<Question>> get(@PathVariable long uid){
        logger.info("Request a list of all questions by uid {}", uid);
        return ResponseEntity.ok(service.findByUserId(uid));
    }
}
