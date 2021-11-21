package de.haevn.distributed_systems.questions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static de.haevn.distributed_systems.Constants.*;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@AutoConfigureMockMvc
class DeleteQuestionsTest extends QuestionsTestConfiguration {

    @Test
    void deleteAllProduct(){
        logStart(this.getClass());

        doNothing().when(repository).deleteAll();


        logEnd(this.getClass());
    }

    @Test
    void deleteProductById(){
        logStart(this.getClass());

        doNothing().when(repository).deleteById(1L);

        var result = Assertions.assertDoesNotThrow(
                () -> controller.deleteById(0),
                exceptionShouldNotOccur
        );
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));

        logEnd(this.getClass());
    }

    @Test
    void deleteProductByInvalidId(){
        logStart(this.getClass());

        doNothing().when(repository).deleteById(-1L);

        var result = Assertions.assertDoesNotThrow(
                () -> controller.deleteById(-1L),
                exceptionShouldNotOccur
        );
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));

        logEnd(this.getClass());
    }
}
