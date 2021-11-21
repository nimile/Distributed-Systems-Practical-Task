package de.haevn.distributed_systems.user;

import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.exceptions.found.NoSuchUserException;
import de.haevn.distributed_systems.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static de.haevn.distributed_systems.Constants.*;

@SpringBootTest
@AutoConfigureMockMvc
class PutUserTest extends UserTestConfiguration {

    @Test
    void putAllUserTest(){
        logStart(this.getClass());

        Optional<ArrayList<User>> requestBody = Optional.of(new ArrayList<>(users));
        when(repository.findAll()).thenReturn(users);

        var result = Assertions.assertDoesNotThrow(
                () -> controller.put(requestBody),
                exceptionShouldNotOccur);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));

        logEnd(this.getClass());
    }

    @Test
    void putAllUserWithEmptyBodyTest(){
        logStart(this.getClass());

        Optional<ArrayList<User>> requestBody = Optional.empty();
        when(repository.findAll()).thenReturn(users);

        Assertions.assertThrows(ArgumentMismatchException.class,
                () -> controller.put(requestBody),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }

    @Test
    void putUserById(){
        logStart(this.getClass());

        when(repository.findById(1L)).thenReturn(Optional.of(users.get(0)));

        var result = Assertions.assertDoesNotThrow(
                ()-> controller.getById(1),
                exceptionShouldNotOccur);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.OK.name()));

        logEnd(this.getClass());
    }

    @Test
    void putUserByInvalidId(){
        logStart(this.getClass());

        when(repository.findById(0L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchUserException.class,
                () -> controller.getById(0L),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }
}
