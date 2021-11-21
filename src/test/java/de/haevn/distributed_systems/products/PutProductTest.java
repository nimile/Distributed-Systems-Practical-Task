package de.haevn.distributed_systems.products;

import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.exceptions.found.NoSuchProductException;
import de.haevn.distributed_systems.model.Product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Optional;

import static de.haevn.distributed_systems.Constants.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class PutProductTest extends ProductTestConfiguration {

    @Test
    void putAllProduct(){
        logStart(this.getClass());

        Optional<ArrayList<Product>> requestBody = Optional.of(new ArrayList<>(products));
        when(repository.findAll()).thenReturn(products);

        var result = Assertions.assertDoesNotThrow(
                () -> controller.put(requestBody),
                exceptionShouldNotOccur);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));

        logEnd(this.getClass());
    }

    @Test
    void putAllProductWithEmptyBodyTest(){
        logStart(this.getClass());

        Optional<ArrayList<Product>> requestBody = Optional.empty();
        when(repository.findAll()).thenReturn(products);

        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.put(requestBody),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }

    @Test
    void putProductById(){
        logStart(this.getClass());

        when(repository.findById(1L)).thenReturn(Optional.of(products.get(0)));

        var result = Assertions.assertDoesNotThrow(
                ()-> controller.getById(1),
                exceptionShouldNotOccur);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));

        logEnd(this.getClass());
    }

    @Test
    void putProductByInvalidId(){
        logStart(this.getClass());

        when(repository.findById(0L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchProductException.class,
                () -> controller.getById(0L),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }
}
