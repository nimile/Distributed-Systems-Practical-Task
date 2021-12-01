package de.haevn.distributed_systems.v2.product;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.ProductController;
import de.haevn.distributed_systems.v2.exceptions.APIException;
import de.haevn.distributed_systems.v2.exceptions.ArgumentMismatchException;
import de.haevn.distributed_systems.v2.exceptions.ConflictException;
import de.haevn.distributed_systems.v2.interfaces.AbstractPutTest;
import de.haevn.distributed_systems.v2.model.Product;
import de.haevn.distributed_systems.v2.model.User;
import de.haevn.distributed_systems.v2.repository.ProductRepository;
import de.haevn.distributed_systems.v2.service.ProductService;
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
public class ProductPutTest extends AbstractPutTest<Product> {
    public static final Logger logger = LoggerFactory.getLogger(ProductPutTest.class);

    @Autowired
    public ProductController controller;

    @MockBean
    public ProductRepository repository;

    @MockBean
    public ProductService service;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    void initData(){
        // Setup data

        data.clear();
        data.add(Product.builder().name("Generic Keyboard").brand("Generic Brand").newPrice(169.99).oldPrice(169.99).id(0L).build());
        data.add(Product.builder().name("Generic Mouse").brand("Generic Brand").newPrice(69.99).oldPrice(69.99).id(1L).build());
        data.add(Product.builder().name("Deluxe Keyboard").brand("Deluxe Brand").newPrice(159.99).oldPrice(269.99).id(2L).build());

        optionalObject = Optional.of(data.get(0));
        emptyObject = Optional.empty();
        emptyObjectList = Optional.empty();
        optionalList = Optional.of(new ArrayList<>(data));

        logger.info("Setup repositories");
        when(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME)).thenReturn(1L);

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
        initData();
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
        initData();

        logger.info("Execute test");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.put(emptyObjectList),
                "Custom message"
        );

    }



    @Test
    @Override
    public void putByIdArgumentMismatchException(){
        initData();

        logger.info("Execute test");
        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.putById(1, emptyObject),
                "Custom message"
        );
    }

    @Test
    @Override
    public void putByIdConflictException(){
        initData();
        logger.info("Execute test");
        Optional<Product> user = Optional.of(data.get(0));
        long id = user.get().getId() + 1;
        Assertions.assertThrows(ConflictException.class,
                () -> controller.putById(id, user),
                "Custom message"
        );
    }

    @Test
    @Override
    public void putByIdApiException(){
        Optional<Product> user = Optional.of(data.get(0));
        long id = user.get().getId();
        when(service.update(user.get())).thenReturn(emptyObject);

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
        Optional<Product> user = Optional.of(data.get(0));
        when(service.update(user.get())).thenReturn(user);

        logger.info("Execute test");
        var result = Assertions.assertDoesNotThrow(
                () -> controller.putById(user.get().getId(), user),
                "Custom Message"
        ).getStatusCode();

        Assertions.assertEquals(
                HttpStatus.OK, result,
                "Custom Message"
        );
    }
}
