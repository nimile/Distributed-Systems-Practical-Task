package de.haevn.distributed_systems.v2.user;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.UserController;
import de.haevn.distributed_systems.v2.exceptions.APIException;
import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import de.haevn.distributed_systems.v2.exceptions.ConflictException;
import de.haevn.distributed_systems.v2.interfaces.AbstractPutTest;
import de.haevn.distributed_systems.v2.model.User;
import de.haevn.distributed_systems.v2.repository.UserRepository;
import de.haevn.distributed_systems.v2.service.UserService;
import de.haevn.distributed_systems.v2.utils.sequence_generator.SequenceGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = DistributedSystemsApplication.class)
@AutoConfigureMockMvc
public class UserPutTest extends AbstractPutTest<User> {
    public static final Logger logger = LoggerFactory.getLogger(UserPutTest.class);

    @Autowired
    public UserController controller;

    @MockBean
    public UserRepository repository;

    @MockBean
    public UserService service;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    void initData(){
        logger.info("Setup data");
        // Setup data
        data.clear();
        data.add(User.builder().firstname("Test1").lastname("User1").email("U1@test.domain").address("Street No.1").password("1234").id(1L).build());
        data.add(User.builder().firstname("Test2").lastname("User2").email("U2@test.domain").address("Street No.2").password("1234").id(2L).build());
        data.add(User.builder().firstname("Test3").lastname("User3").email("U3@test.domain").address("Street No.3").password("1234").id(3L).build());

        optionalObject = Optional.of(data.get(0));

        logger.info("Setup repositories");
        when(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME)).thenReturn(1L);

        // Setup repository
        when(repository.findByEmail(data.get(0).getEmail())).thenReturn(Optional.of(data.get(0)));
        when(repository.findByEmail(data.get(1).getEmail())).thenReturn(Optional.of(data.get(1)));
        when(repository.findByEmail(data.get(2).getEmail())).thenReturn(Optional.of(data.get(2)));


        when(repository.save(data.get(0))).thenReturn(data.get(0));
        when(repository.save(data.get(1))).thenReturn(data.get(1));
        when(repository.save(data.get(2))).thenReturn(data.get(2));

        when(repository.findAll()).thenReturn(data);
    }

    @Test
    @Override
    public void put() {
        initData();
        when(service.update(data)).thenReturn(3L);
        Optional<ArrayList<User>> user = Optional.of(new ArrayList<>(data));

        logger.info("Execute test");
        Assertions.assertDoesNotThrow(
                () -> controller.put(user),
                "Custom Message"
        );
    }

    @Test
    @Override
    public void putThrowsArgumentMismatchException() {
        initData();
        Optional<ArrayList<User>> emptyList = Optional.empty();

        logger.info("Execute test");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.put(emptyList),
                "Custom message"
        );

    }



    @Test
    @Override
    public void putByIdArgumentMismatchException(){
        initData();
        Optional<User> emptyUser = Optional.empty();

        logger.info("Execute test");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.putById(1, emptyUser),
                "Custom message"
        );
    }

    @Test
    @Override
    public void putByIdConflictException(){
        initData();
        logger.info("Execute test");
        Optional<User> user = Optional.of(data.get(0));
        long id = user.get().getId() + 1;
        Assertions.assertThrows(ConflictException.class,
                () -> controller.putById(id, user),
                "Custom message"
        );
    }

    @Test
    @Override
    public void putByIdApiException(){
        Optional<User> user = Optional.of(data.get(0));
        long id = user.get().getId();
        when(service.update(user.get())).thenReturn(Optional.empty());

        logger.info("Execute test");
        Assertions.assertThrows(
                APIException.class,
                () -> controller.putById(id, user),
                "Custom message"
        );
    }


    @Test
    @Override
    public void putById() {
        initData();
        Optional<User> user = Optional.of(data.get(0));
        when(service.update(user.get())).thenReturn(user);

        logger.info("Execute test");
        Assertions.assertDoesNotThrow(
                () -> controller.putById(user.get().getId(), user),
                "Custom Message"
        );
    }
}
