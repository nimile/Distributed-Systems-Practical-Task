package de.haevn.distributed_systems.products;

import de.haevn.distributed_systems.exceptions.found.NoSuchProductException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static de.haevn.distributed_systems.Constants.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class GetProductTest extends ProductTestConfiguration {

    @Test
    void getAllProduct(){
        logStart(this.getClass());
        when(repository.findAll()).thenReturn(products);

        var result = Assertions.assertDoesNotThrow(
                () -> controller.get(),
                exceptionShouldNotOccur);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));
        Assertions.assertEquals(products.size(), result.getBody().size(),
                "Result size is differs from given size");

        logEnd(this.getClass());
    }

    @Test
    void getProductById(){
        logStart(this.getClass());

        when(repository.findById(1L)).thenReturn(Optional.of(products.get(0)));

        var result = Assertions.assertDoesNotThrow(
                () -> controller.getById(1L),
                exceptionShouldNotOccur);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));

        logEnd(this.getClass());
    }

    @Test
    void getProductByInvalidId(){
        logStart(this.getClass());

        when(repository.findById(-1L)).thenReturn(Optional.empty());

        var result = Assertions.assertThrows(
                NoSuchProductException.class,
                () -> controller.getById(-1L),
                exceptionShouldOccur);
        logEnd(this.getClass());
    }
}
