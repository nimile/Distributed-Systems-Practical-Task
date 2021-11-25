package de.haevn.distributed_systems.v2.question;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.QuestionController;
import de.haevn.distributed_systems.v2.controller.ReviewController;
import de.haevn.distributed_systems.v2.exceptions.NoObjectExistsException;
import de.haevn.distributed_systems.v2.interfaces.AbstractGetTest;
import de.haevn.distributed_systems.v2.model.Question;
import de.haevn.distributed_systems.v2.model.Review;
import de.haevn.distributed_systems.v2.model.User;
import de.haevn.distributed_systems.v2.repository.QuestionRepository;
import de.haevn.distributed_systems.v2.repository.ReviewRepository;
import de.haevn.distributed_systems.v2.service.QuestionService;
import de.haevn.distributed_systems.v2.service.ReviewService;
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

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = DistributedSystemsApplication.class)
@AutoConfigureMockMvc
public class QuestionGetTest extends AbstractGetTest<Question> {
    public static final Logger logger = LoggerFactory.getLogger(QuestionGetTest.class);

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
        logger.info("Setup data");
        // Setup data

        Optional<User> testUser = Optional.of(User.builder().firstname("Test1").lastname("User1").email("U1@test.domain").address("Street No.1").password("1234").id(1L).build());
        when(userService.findByEmail("U1@test.domain")).thenReturn(testUser);

        data.add(Question.builder().id(1L).firstname("Test1").lastname("User1").email("U1@test.domain").category("Retoure").description("Description").build());
        data.add(Question.builder().id(2L).firstname("Test2").lastname("User2").email("U2@test.domain").category("Feedback").description("Description").build());
        data.add(Question.builder().id(3L).firstname("Test3").lastname("User3").email("U3@test.domain").category("Help").description("Description").build());

        optionalObject = Optional.of(data.get(0));
        emptyObject = Optional.empty();
        emptyObjectList = Optional.empty();

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
    public void get() {
        initData();
        when(service.findAll()).thenReturn(data);

        logger.info("Execute test");
        var result = Assertions.assertDoesNotThrow(
                () -> controller.get(),
                "Custom Message"
        ).getStatusCode();

        Assertions.assertEquals(
                HttpStatus.OK, result,
                "Custom Message"
        );
    }

    @Test
    @Override
    public void getById() {
        initData();
        when(service.findById(1L)).thenReturn(Optional.of(data.get(0)));

        logger.info("Execute test");
        var result = Assertions.assertDoesNotThrow(
                () -> controller.getById(1L),
                "Custom Message"
        ).getStatusCode();

        Assertions.assertEquals(
                HttpStatus.OK, result,
                "Custom Message"
        );
    }

    @Test
    @Override
    public void getByIdObjectDoesNotExists() {
        initData();
        when(service.findById(0L)).thenReturn(emptyObject);

        logger.info("Execute test");
        Assertions.assertThrows(
                NoObjectExistsException.class,
                () -> controller.getById(0L),
                "Custom Message"
        );
    }
}
