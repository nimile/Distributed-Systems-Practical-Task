package de.haevn.distributed_systems.questions;

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
class PutQuestionsTest extends QuestionsTestConfiguration {

    @Test
    void putAllProduct(){
        logStart(this.getClass());

        logEnd(this.getClass());
    }

    @Test
    void putAllProductWithEmptyBodyTest(){
        logStart(this.getClass());


        logEnd(this.getClass());
    }

    @Test
    void putProductById(){
        logStart(this.getClass());

        logEnd(this.getClass());
    }

    @Test
    void putProductByInvalidId(){
        logStart(this.getClass());

        logEnd(this.getClass());
    }
}
