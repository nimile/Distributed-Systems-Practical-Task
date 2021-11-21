package de.haevn.distributed_systems.user;

import de.haevn.distributed_systems.exceptions.ExistenceException;
import de.haevn.distributed_systems.exceptions.ForbiddenException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static de.haevn.distributed_systems.Constants.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostUserTest extends UserTestConfiguration {

    @Test
    void postUserWithMissingArguments() {
        logStart(this.getClass());

        assertThrows(ArgumentMismatchException.class,
                () -> controller.post(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }

    @Test
    void postUserExisting() {
        logStart(this.getClass());

        User user = users.get(0);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(ExistenceException.class,
                () -> controller.post(
                        Optional.of(user.getFirstname()),
                        Optional.of(user.getLastname()),
                        Optional.of(user.getEmail()),
                        Optional.of(user.getAddress()),
                        Optional.of(user.getPassword())),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }

    @Test
    void postUser() throws ExistenceException {
        logStart(this.getClass());

        User user = users.get(0);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(repository.save(user)).thenReturn(user);
        when(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME)).thenReturn(0L);

        var result = Assertions.assertDoesNotThrow(
                () -> controller.post(
                        Optional.of(user.getFirstname()),
                        Optional.of(user.getLastname()),
                        Optional.of(user.getEmail()),
                        Optional.of(user.getAddress()),
                        Optional.of(user.getPassword())),
                exceptionShouldNotOccur);
        assertEquals(HttpStatus.CREATED, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.CREATED.name()));

        logEnd(this.getClass());
    }

    @Test
    void postUserById() {
        logStart(this.getClass());

        assertThrows(ForbiddenException.class,
                () -> controller.postById(1),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }
}
