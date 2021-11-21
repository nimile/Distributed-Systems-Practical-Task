package de.haevn.distributed_systems.controller;


import de.haevn.distributed_systems.exceptions.ExistenceException;
import de.haevn.distributed_systems.exceptions.ForbiddenException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.exceptions.found.NoSuchUserException;
import de.haevn.distributed_systems.exceptions.found.SecurityViolationException;
import de.haevn.distributed_systems.model.User;
import de.haevn.distributed_systems.service.UserService;
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
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    /**
     * Email must be unique otherwise an exception is thrown
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     * @param password
     * @return
     */
    @PostMapping(produces = "application/json")
    public ResponseEntity<User> post(@RequestParam Optional<String> firstName,
                                     @RequestParam Optional<String> lastName,
                                     @RequestParam Optional<String> email,
                                     @RequestParam Optional<String> address,
                                     @RequestParam Optional<String> password) throws ExistenceException, ArgumentMismatchException {
        var user = service.save(firstName.orElse(null), lastName.orElse(null), email.orElse(null), address.orElse(null), password.orElse(null));
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<User>> get(){
        logger.info("Request a list of all users");
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Takes a json array of users as body and updates all user according to changed values
     * @param objs
     * @return
     */
    @PutMapping(produces = "application/json")
    public ResponseEntity<String> put(@RequestBody Optional<ArrayList<User>> objs) throws ArgumentMismatchException {
        logger.info("Update users");
        CustomOptionalUtils.containsEmptyOptional(objs);
        long updates = service.update(objs.orElse(new ArrayList<>()));
        return ResponseEntity.status(HttpStatus.OK).body("{\"Updated users\":\"" + updates + "\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(){
        logger.warn("Delete all users invoked");
        service.deleteAll();
        sequenceGeneratorService.resetSequence(User.SEQUENCE_NAME);
        return ResponseEntity.ok().build();
    }



    @PostMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> postById(@PathVariable long id){
        logger.error("Invoke illegal post operation");
        throw new ForbiddenException();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<User> getById(@PathVariable long id){
        logger.info("Request user object with id {}", id);
        User user = service.findById(id).orElseThrow(NoSuchUserException::new);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<User> putById(@PathVariable long id, @RequestBody Optional<User> obj) throws ArgumentMismatchException {
        logger.info("Update user with id {}", id);
        CustomOptionalUtils.containsEmptyOptional(obj);

        // Extract the user from the argument
        var targetUser = obj.orElse(User.builder().build());

        // Lookup the user
        Optional<User> userByEmail = service.findByEmail(targetUser.getEmail());
        if(userByEmail.isEmpty()){
            // User was not found => throw new NoSuchUserException
            logger.warn("No user found with id {}", id);
            throw new NoSuchUserException();
        }else if(userByEmail.get().getId() != id){
            // User was found but the email does not match the given id => throw new ConflictException
            logger.error("Security violation, given id {} does not match the provided email address id {}", id, userByEmail.get().getId());
            throw new SecurityViolationException();
        }else if(targetUser.getId() != id){
            // User was found but the email does not match the given id => throw new ConflictException
            logger.error("ID violation. given id {} does not match body id {}", id, targetUser.getId());
            throw new SecurityViolationException();
        }

        logger.info("User updated");
        service.update(targetUser);
        return ResponseEntity.status(HttpStatus.OK).body(targetUser);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> deleteById(@PathVariable long id){
        logger.warn("Delete user {} invoked", id);
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
