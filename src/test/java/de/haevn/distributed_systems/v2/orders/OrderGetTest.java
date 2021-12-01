package de.haevn.distributed_systems.v2.orders;

import de.haevn.distributed_systems.DistributedSystemsApplication;
import de.haevn.distributed_systems.v2.controller.OrderController;
import de.haevn.distributed_systems.v2.exceptions.NoObjectExistsException;
import de.haevn.distributed_systems.v2.interfaces.AbstractGetTest;
import de.haevn.distributed_systems.v2.model.Order;
import de.haevn.distributed_systems.v2.repository.OrderRepository;
import de.haevn.distributed_systems.v2.service.OrderService;
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
public class OrderGetTest extends AbstractGetTest<Order> {
    public static final Logger logger = LoggerFactory.getLogger(OrderGetTest.class);

    @Autowired
    public OrderController controller;

    @MockBean
    public OrderRepository repository;

    @MockBean
    public OrderService service;

    @MockBean
    public SequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    void initData(){
        logger.info("Setup data");
        // Setup data

        data.clear();
        data.add(Order.builder().id(1L).date("01-01-2021").payment("Paypal").total(100.99).build());
        data.add(Order.builder().id(2L).date("01-01-2021").payment("Credit").total(249.99).build());
        data.add(Order.builder().id(3L).date("01-01-2021").payment("Cash").total(69.99).build());

        optionalObject = Optional.of(data.get(0));
        emptyObject = Optional.empty();
        emptyObjectList = Optional.empty();

        logger.info("Setup repositories");
        when(sequenceGeneratorService.generateSequence(Order.SEQUENCE_NAME)).thenReturn(1L);

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
