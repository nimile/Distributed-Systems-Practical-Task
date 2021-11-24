package de.haevn.distributed_systems.v2.user;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.UserController;
import de.haevn.distributed_systems.v2.exceptions.NoObjectExistsException;
import de.haevn.distributed_systems.v2.interfaces.AbstractGetTest;
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
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = DistributedSystemsApplication.class)
@AutoConfigureMockMvc
public class UserGetTest extends AbstractGetTest<User> {
    public static final Logger logger = LoggerFactory.getLogger(UserGetTest.class);

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
    public void get() {
        initData();
        when(service.findAll()).thenReturn(data);

        logger.info("Execute test");
        Assertions.assertDoesNotThrow(
                () -> controller.get(),
                "Custom Message"
        );
    }

    @Test
    @Override
    public void getById() {
        initData();
        when(service.findById(1L)).thenReturn(Optional.of(data.get(0)));

        logger.info("Execute test");
        Assertions.assertDoesNotThrow(
                () -> controller.getById(1L),
                "Custom Message"
        );
    }

    @Test
    @Override
    public void getByIdObjectDoesNotExists() {
        initData();
        when(service.findById(0L)).thenReturn(Optional.empty());

        logger.info("Execute test");
        Assertions.assertThrows(
                NoObjectExistsException.class,
                () -> controller.getById(0L),
                "Custom Message"
        );
    }
}
