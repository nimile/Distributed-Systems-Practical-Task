package de.haevn.distributed_systems.v2.controller;

import de.haevn.distributed_systems.v2.exceptions.*;
import de.haevn.distributed_systems.v2.model.User;
import de.haevn.distributed_systems.v2.service.UserService;
import de.haevn.distributed_systems.v2.utils.GenericResultContainer;
import de.haevn.distributed_systems.v2.utils.AppUtils;
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
@RequestMapping(value = "/api/v2/users", produces = "application/json")
public class UserController implements IController<User>{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private UserService service;

    @PostMapping
    @Override
    public ResponseEntity<User> post(@RequestBody Optional<User> obj) throws APIException{
        // Checks if the body exists and contains an email,
        // if the validation fails a new ArgumentMismatchException, containing UNPROCESSABLE_ENTITY, is thrown
        // The second validation is for an existing user. When a user exists an ExistingException, is thrown
        if(obj.isEmpty() || obj.get().getEmail() == null || obj.get().getEmail().isBlank() || obj.get().getEmail().isEmpty()){
            throw new ArgumentMismatchException();
        }else if(service.findByEmail(obj.get().getEmail()).isPresent()){
            throw new ExistenceException();
        }
        // Set an automatic generated ID
        // NOTE If the database supports autogenerate IDs this line is no longer valid
        obj.get().setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));

        // Tries to save the object, this operation fails when the user already exists
        if(service.save(obj.get())) {
            var result = service.findByEmail(obj.get().getEmail());
            // This check should always be true, but in some edge cases, e.g. DB shutdown, this can be false
            if (result.isPresent()) {
                return AppUtils.generateResponse(HttpStatus.OK, result.get());
            }
        }
        logger.error("Provided object ({}) generated an internal server error.", obj);
        throw new APIException();
    }

    @GetMapping
    @Override
    public ResponseEntity<List<User>> get() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @PutMapping
    @Override
    public ResponseEntity<?> put(@RequestBody Optional<ArrayList<User>> objs) throws ArgumentMismatchException {
        // If the request body is empty return an ArgumentMismatchException
        if(objs.isEmpty()){
            throw new ArgumentMismatchException();
        }
        // Call the update service and construct a result object
        var updates = service.update(objs.get());
        GenericResultContainer<Long> container = new GenericResultContainer<>();
        container.setMessage("Updated users");
        container.setData(updates);
        return AppUtils.generateResponse(HttpStatus.OK, container);
    }

    @DeleteMapping
    @Override
    public void delete() {
        sequenceGeneratorService.resetSequence(User.SEQUENCE_NAME);
        service.delete();
    }


    @PostMapping("/{id}")
    @Override
    public ResponseEntity<String> postById(@PathVariable long id) throws APIException {
        throw new ForbiddenException();
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<User> getById(@PathVariable long id) {
        var result = service.findById(id);
        // If query was empty return the user does not exist => throw a new ExistenceException
        if(result.isEmpty()){
            throw new NoObjectExistsException();
        }
        return AppUtils.generateResponse(HttpStatus.OK, result.get());
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<User> putById(@PathVariable long id, @RequestBody Optional<User> obj) throws ArgumentMismatchException {
        // Check if a body was provided
        // If the body is missing throw a new ArgumentMismatchException
        // If the path variable does not match the id of the object throw a new ConflictException
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
