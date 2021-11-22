package de.haevn.distributed_systems.v2.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "questions")
public class Question {
    @Transient
    public static final String SEQUENCE_NAME = "question_sequence";

    @Id
    private Long id;
    private Long customerId;
    private String firstname;
    private String lastname;
    private String email;
    private String subject;
    private String category;
    private String description;
}
