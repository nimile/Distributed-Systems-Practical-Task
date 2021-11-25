package de.haevn.distributed_systems.v2.question;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.QuestionController;
import de.haevn.distributed_systems.v2.exceptions.APIException;
import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import de.haevn.distributed_systems.v2.exceptions.ConflictException;
import de.haevn.distributed_systems.v2.interfaces.AbstractPutTest;
import de.haevn.distributed_systems.v2.model.Question;
import de.haevn.distributed_systems.v2.model.Review;
import de.haevn.distributed_systems.v2.model.User;
import de.haevn.distributed_systems.v2.repository.QuestionRepository;
import de.haevn.distributed_systems.v2.service.QuestionService;
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
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = DistributedSystemsApplication.class)
@AutoConfigureMockMvc
public class QuestionPutTest extends AbstractPutTest<Question> {
    public static final Logger logger = LoggerFactory.getLogger(QuestionPutTest.class);

    @Autowired
    public QuestionController controller;

    @MockBean
    public QuestionRepository repository;

    @MockBean
    public QuestionService service;

    @MockBean
    public UserService userService;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    void initData(){
        // Setup data

        data.clear();
        Optional<User> testUser = Optional.of(User.builder().firstname("Test1").lastname("User1").email("U1@test.domain").address("Street No.1").password("1234").id(1L).build());
        when(userService.findByEmail("U1@test.domain")).thenReturn(testUser);

        data.add(Question.builder().id(1L).firstname("Test1").lastname("User1").email("U1@test.domain").category("Return").description("Description").build());
        data.add(Question.builder().id(2L).firstname("Test2").lastname("User2").email("U2@test.domain").category("Feedback").description("Description").build());
        data.add(Question.builder().id(3L).firstname("Test3").lastname("User3").email("U3@test.domain").category("Help").description("Description").build());

        optionalObject = Optional.of(data.get(0));
        emptyObject = Optional.empty();
        emptyObjectList = Optional.empty();
        optionalList = Optional.of(new ArrayList<>(data));

        logger.info("Setup repositories");
        when(sequenceGeneratorService.generateSequence(Review.SEQUENCE_NAME)).thenReturn(1L);

        // Setup repository
        when(repository.findById(data.get(0).getId())).thenReturn(Optional.of(data.get(0)));
        when(repository.findById(data.get(1).getId())).thenReturn(Optional.of(data.get(1)));
        when(repository.findById(data.get(2).getId())).thenReturn(Optional.of(data.get(2)));


        when(repository.save(data.get(0))).thenReturn(data.get(0));
        when(repository.save(data.get(1))).thenReturn(data.get(1));
        when(repository.save(data.get(2))).thenReturn(data.get(2));

        when(repository.findAll()).thenReturn(data);
    }

    @Test
    @Override
    public void put() {
        when(service.update(data)).thenReturn(3L);

        logger.info("Execute test");
        var result = Assertions.assertDoesNotThrow(
                () -> controller.put(optionalList),
                "Custom Message"
        ).getStatusCode();

        Assertions.assertEquals(
                HttpStatus.OK, result,
                "Custom Message"
        );
    }

    @Test
    @Override
    public void putThrowsArgumentMismatchException() {
        logger.info("Execute test");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.put(emptyObjectList),
                "Custom message"
        );

    }



    @Test
    @Override
    public void putByIdArgumentMismatchException(){
        logger.info("Execute test");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.putById(1, emptyObject),
                "Custom message"
        );
    }

    @Test
    @Override
    public void putByIdConflictException(){
        logger.info("Execute test");
        Optional<Question> question = Optional.of(data.get(0));
        long id = question.get().getId() + 1;
        Assertions.assertThrows(ConflictException.class,
                () -> controller.putById(id, question),
                "Custom message"
        );
    }

    @Test
    @Override
    public void putByIdApiException(){
        Optional<Question> question = Optional.of(data.get(0));
        long id = question.get().getId();
        when(service.update(question.get())).thenReturn(emptyObject);

        logger.info("Execute test");
        Assertions.assertThrows(
                APIException.class,
                () -> controller.putById(id, question),
                "Custom message"
        );
    }

    @Test
    @Override
    public void putById() {
        Optional<Question> question = Optional.of(data.get(0));
        when(service.update(question.get())).thenReturn(question);

        logger.info("Execute test");
        var result = Assertions.assertDoesNotThrow(
                () -> controller.putById(question.get().getId(), question),
                "Custom Message"
        ).getStatusCode();

        Assertions.assertEquals(
                HttpStatus.OK, result,
                "Custom Message"
        );
    }
}
