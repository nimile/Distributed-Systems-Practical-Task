package de.haevn.distributed_systems.products;

import de.haevn.distributed_systems.exceptions.ForbiddenException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static de.haevn.distributed_systems.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostProductTest extends ProductTestConfiguration {

    @Test
    void postProductWithMissingArguments() {
        logStart(this.getClass());

        assertThrows(ArgumentMismatchException.class,
                () -> controller.post(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }

    @Test
    void postProduct() {
        logStart(this.getClass());
        Product product = products.get(0);

        var result = assertDoesNotThrow(
                () -> controller.post(Optional.of(product.getName()),
                        Optional.of(product.getBrand()),
                        Optional.of(product.getDescription()),
                        Optional.of(String.valueOf(product.getNewPrice()))),
                exceptionShouldNotOccur
        );

        assertEquals(HttpStatus.CREATED, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.CREATED.name()));

        logEnd(this.getClass());
    }

    @Test
    void postProductById() {
        logStart(this.getClass());

        assertThrows(ForbiddenException.class,
                () -> controller.postById(1L),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }
}
