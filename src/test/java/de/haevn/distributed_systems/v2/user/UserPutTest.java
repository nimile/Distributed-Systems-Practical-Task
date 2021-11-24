package de.haevn.distributed_systems.v2.user;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.UserController;
import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import de.haevn.distributed_systems.v2.exceptions.ExistenceException;
import de.haevn.distributed_systems.v2.exceptions.ForbiddenException;
import de.haevn.distributed_systems.v2.interfaces.AbstractPostTest;
import de.haevn.distributed_systems.v2.interfaces.AbstractPutTest;
import de.haevn.distributed_systems.v2.model.User;
import de.haevn.distributed_systems.v2.repository.UserRepository;
import de.haevn.distributed_systems.v2.service.UserService;
import de.haevn.distributed_systems.v2.utils.sequence_generator.SequenceGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public void post() {
        AtomicBoolean saved = new AtomicBoolean(false);
        logger.info("Setup service");
        when(service.findByEmail(data.get(0).getEmail())).thenReturn(Optional.of(data.get(0)));
        when(service.findByEmail(data.get(0).getEmail())).thenAnswer((Answer<?>) invocation ->{
            if(saved.get()){
                return Optional.of(data.get(0));
            }else{
                return Optional.empty();
            }
        });
        when(service.save(data.get(0))).thenAnswer(
                (Answer) invocation -> {
                    saved.set(true);
                    return true;
                }
        );

        logger.info("Execute test");
        var result= Assertions.assertDoesNotThrow(
                () -> controller.post(Optional.of(data.get(0))));
        System.out.println(result.getBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                "Custom message");
    }

    @Test
    public void postThrowsArgumentMismatchException() {
        logger.info("Execute test");
        Optional<User> invalidUser = Optional.of(data.get(0));

        logger.info("Test empty object");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.post(Optional.empty()),
                "Custom message");

        logger.info("Test blank email address");
        invalidUser.get().setEmail("");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.post(invalidUser),
                "Custom message");

        logger.info("Test empty email address");
        invalidUser.get().setEmail("  ");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.post(invalidUser),
                "Custom message");

        logger.info("Test null email address");
        invalidUser.get().setEmail(null);
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.post(invalidUser),
                "Custom message");
    }

    @Test
    public void postExistsException() {
        when(service.findByEmail(data.get(0).getEmail())).thenReturn(Optional.of(data.get(0)));
        var user = Optional.of(data.get(0));
        logger.info("Execute test");

        Assertions.assertThrows(ExistenceException.class,
                () -> controller.post(user),
                "Custom message");
    }

    @Test
    public void postById() {
        initData();
        logger.info("Execute test");
        Assertions.assertThrows(ForbiddenException.class,
                () -> controller.postById(0L),
                "Custom message");
    }

    @Override
    public void put() {
        logger.info("Execute test");
        initData();
    }

    @Override
    public void putThrowsArgumentMismatchException() {
        logger.info("Execute test");
        initData();
        Optional<ArrayList<User>> emptyList = Optional.empty();

        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.put(emptyList),
                "Custom message");

    }

    @Override
    public void putApiException() {
        logger.info("Execute test");
        initData();

    }

    @Override
    public void putById() {
        logger.info("Execute test");
        initData();

    }
}
