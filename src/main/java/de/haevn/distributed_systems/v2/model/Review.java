package de.haevn.distributed_systems.v2.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "reviews")
public class Review {
    @Transient
    public static final String SEQUENCE_NAME = "review_sequence";

    @Id
    private Long id;
    private String publisher;
    private Long starRating;
    private String text;
}
