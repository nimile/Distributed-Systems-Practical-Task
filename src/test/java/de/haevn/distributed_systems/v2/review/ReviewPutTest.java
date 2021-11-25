package de.haevn.distributed_systems.v2.review;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.ReviewController;
import de.haevn.distributed_systems.v2.exceptions.APIException;
import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import de.haevn.distributed_systems.v2.exceptions.ConflictException;
import de.haevn.distributed_systems.v2.interfaces.AbstractPutTest;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = DistributedSystemsApplication.class)
@AutoConfigureMockMvc
public class ReviewPutTest extends AbstractPutTest<Review> {
    public static final Logger logger = LoggerFactory.getLogger(ReviewPutTest.class);

    @Autowired
    public ReviewController controller;

    @MockBean
    public ReviewRepository repository;

    @MockBean
    public ReviewService service;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    void initData(){
        // Setup data

        data.clear();
        data.add(Review.builder().id(0L).publisher("User 1").starRating(5L).text("Example Review").build());
        data.add(Review.builder().id(1L).publisher("User 2").starRating(4L).text("Example Review").build());
        data.add(Review.builder().id(2L).publisher("User 3").starRating(3L).text("Example Review").build());

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
        Optional<Review> review = Optional.of(data.get(0));
        long id = review.get().getId() + 1;
        Assertions.assertThrows(ConflictException.class,
                () -> controller.putById(id, review),
                "Custom message"
        );
    }

    @Test
    @Override
    public void putByIdApiException(){
        Optional<Review> review = Optional.of(data.get(0));
        long id = review.get().getId();
        when(service.update(review.get())).thenReturn(emptyObject);

        logger.info("Execute test");
        Assertions.assertThrows(
                APIException.class,
                () -> controller.putById(id, review),
                "Custom message"
        );
    }

    @Test
    @Override
    public void putById() {
        Optional<Review> review = Optional.of(data.get(0));
        when(service.update(review.get())).thenReturn(review);

        logger.info("Execute test");
        var result = Assertions.assertDoesNotThrow(
                () -> controller.putById(review.get().getId(), review),
                "Custom Message"
        ).getStatusCode();

        Assertions.assertEquals(
                HttpStatus.OK, result,
                "Custom Message"
        );
    }
}
