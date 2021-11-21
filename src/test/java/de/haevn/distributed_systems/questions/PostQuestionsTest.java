package de.haevn.distributed_systems.questions;

import de.haevn.distributed_systems.exceptions.ForbiddenException;
import de.haevn.distributed_systems.exceptions.found.ArgumentMismatchException;
import de.haevn.distributed_systems.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static de.haevn.distributed_systems.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostQuestionsTest extends QuestionsTestConfiguration {

    @Test
    void postQuestionWithMissingArguments() {
        logStart(this.getClass());

        assertThrows(ArgumentMismatchException.class,
                () -> controller.post(Optional.empty()),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }

    @Test
    void postQuestion() {
        logStart(this.getClass());

        var question = questions.get(0);

        var result = Assertions.assertDoesNotThrow(
                () -> controller.post(Optional.of(question)),
                exceptionShouldNotOccur
        );

        assertEquals(HttpStatus.CREATED, result.getStatusCode(),
                String.format(httpStatusTypeFormatMessage, result.getStatusCode().name(), HttpStatus.CREATED.name()));

        logEnd(this.getClass());
    }

    @Test
    void postQuestionById() {
        logStart(this.getClass());

        assertThrows(ForbiddenException.class,
                () -> controller.postById(1L),
                exceptionShouldOccur);

        logEnd(this.getClass());
    }
}
