package de.haevn.distributed_systems.questions;

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
class GetQuestionsTest extends QuestionsTestConfiguration {

    @Test
    void getAllProduct(){
        logStart(this.getClass());


        logEnd(this.getClass());
    }

    @Test
    void getProductById(){
        logStart(this.getClass());

        logEnd(this.getClass());
    }

    @Test
    void getProductByInvalidId(){
        logStart(this.getClass());

        logEnd(this.getClass());
    }
}
