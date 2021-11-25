package de.haevn.distributed_systems.v2.review;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.ReviewController;
import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import de.haevn.distributed_systems.v2.exceptions.ForbiddenException;
import de.haevn.distributed_systems.v2.interfaces.AbstractPostTest;
import de.haevn.distributed_systems.v2.model.Review;
import de.haevn.distributed_systems.v2.repository.ReviewRepository;
import de.haevn.distributed_systems.v2.service.ReviewService;
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
class ReviewPostTest extends AbstractPostTest<Review> {
    public static final Logger logger = LoggerFactory.getLogger(ReviewPostTest.class);

    @Autowired
    public ReviewController controller;

    @MockBean
    public ReviewRepository repository;

    @MockBean
    public ReviewService service;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    void initData(){   logger.info("Setup data");
        // Setup data

        data.clear();
        data.add(Review.builder().id(0L).publisher("User 1").starRating(5L).text("Example Review").build());
        data.add(Review.builder().id(1L).publisher("User 2").starRating(4L).text("Example Review").build());
        data.add(Review.builder().id(2L).publisher("User 3").starRating(3L).text("Example Review").build());

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
    public void post() {
        logger.info("Setup service");
        when(service.save(data.get(0))).thenReturn(true);

        logger.info("Execute test");
        var result= Assertions.assertDoesNotThrow(
                () -> controller.post(optionalObject),
                "Custom Message"
        );

         Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                "Custom message"
        );
    }

    @Test
    @Override
    public void postThrowsArgumentMismatchException() {
        logger.info("Execute test");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.post(emptyObject),
                "Custom message");
    }

    @Override
    public void postExistsException() {}

    @Test
    @Override
    public void postById() {
        initData();
        logger.info("Execute test");
        Assertions.assertThrows(ForbiddenException.class,
                () -> controller.postById(0L),
                "Custom message");
    }
}
