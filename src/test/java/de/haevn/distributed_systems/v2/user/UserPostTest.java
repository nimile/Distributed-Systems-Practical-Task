package de.haevn.distributed_systems.v2.user;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import de.haevn.distributed_systems.v2.exceptions.ExistenceException;
import de.haevn.distributed_systems.v2.interfaces.AbstractPostTest;
import de.haevn.distributed_systems.v2.controller.UserController;
import de.haevn.distributed_systems.v2.exceptions.ForbiddenException;
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

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = DistributedSystemsApplication.class)
@AutoConfigureMockMvc
class UserPostTest extends AbstractPostTest<User> {
    public static final Logger logger = LoggerFactory.getLogger(UserPostTest.class);

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
        emptyObject = Optional.empty();
        emptyObjectList = Optional.empty();

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
    public void post() {
        AtomicBoolean saved = new AtomicBoolean(false);
        logger.info("Setup service");
        when(service.findByEmail(data.get(0).getEmail())).thenReturn(optionalObject);
        when(service.findByEmail(data.get(0).getEmail())).thenAnswer((Answer<?>) invocation ->{
            if(saved.get()){
                return optionalObject;
            }else{
                return emptyObject;
            }
        });
        when(service.save(data.get(0))).thenAnswer(
                (Answer<Boolean>) invocation -> {
                    saved.set(true);
                    return true;
                }
        );

        logger.info("Execute test");
        var result= Assertions.assertDoesNotThrow(
                () -> controller.post(optionalObject),
                "Custom Message"
        ).getStatusCode();

        Assertions.assertEquals(
                HttpStatus.OK, result,
                "Custom message"
        );
    }

    @Test
    @Override
    public void postThrowsArgumentMismatchException() {
        logger.info("Execute test");
        logger.info("Test empty object");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.post(emptyObject),
                "Custom message");
    }

    @Test
    void postThrowsArgumentMismatchExceptionDueEmptyEmail() {
        logger.info("Execute test");
        Optional<User> invalidUser = Optional.of(data.get(0));
        invalidUser.get().setEmail("  ");

        logger.info("Test empty email address");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.post(invalidUser),
                "Custom message"
        );

    }

    @Test
    void postThrowsArgumentMismatchExceptionDueBlankEmail() {
        logger.info("Execute test");
        Optional<User> invalidUser = Optional.of(data.get(0));
        invalidUser.get().setEmail("");

        logger.info("Test blank email address");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.post(invalidUser),
                "Custom message"
        );
    }

    @Test
    void postThrowsArgumentMismatchExceptionDueNullEmail() {
        logger.info("Execute test");
        Optional<User> invalidUser = Optional.of(data.get(0));
        invalidUser.get().setEmail(null);

        logger.info("Test null email address");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.post(invalidUser),
                "Custom message"
        );
    }

    @Test
    @Override
    public void postExistsException() {
        when(service.findByEmail(data.get(0).getEmail())).thenReturn(Optional.of(data.get(0)));
        var user = Optional.of(data.get(0));

        logger.info("Execute test");
        Assertions.assertThrows(ExistenceException.class,
                () -> controller.post(user),
                "Custom message"
        );
    }

    @Test
    @Override
    public void postById() {
        initData();
        logger.info("Execute test");
        Assertions.assertThrows(ForbiddenException.class,
                () -> controller.postById(0L),
                "Custom message"
        );
    }

}
