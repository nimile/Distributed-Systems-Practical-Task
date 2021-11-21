package de.haevn.distributed_systems.user;

import de.haevn.distributed_systems.exceptions.found.NoSuchUserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static de.haevn.distributed_systems.Constants.*;

@SpringBootTest
@AutoConfigureMockMvc
class GetUserTest extends UserTestConfiguration {

    @Test
    void getAllUser(){
        logStart(this.getClass());

        when(repository.findAll()).thenReturn(users);

        var result= Assertions.assertDoesNotThrow(
                () -> controller.get(),
                exceptionShouldNotOccur);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));
        Assertions.assertEquals(users.size(), result.getBody().size(),
                "Result size is differs from given size");

        logEnd(this.getClass());
    }

    @Test
    void getUserById(){
        logStart(this.getClass());

        when(repository.findById(1L)).thenReturn(Optional.of(users.get(1)));

        var result = Assertions.assertDoesNotThrow(
                () -> controller.getById(1L),
                exceptionShouldNotOccur);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));

        logEnd(this.getClass());
    }

    @Test
    void getUserByInvalidId(){
        logStart(this.getClass());

        when(repository.findById(-1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchUserException.class,
                () -> controller.getById(-1L),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }
}
