package de.haevn.distributed_systems.v2.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "orders")
public class Order {
    @Transient
    public static final String SEQUENCE_NAME = "order_sequence";

    @Id
    private Long id;
    private String date;
    private String payment;
    private Double total;
}
