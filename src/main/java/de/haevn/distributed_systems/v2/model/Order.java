package de.haevn.distributed_systems.v2.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

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

    private User user;
    private List<Product> products;

    public static class OrderInput{
        long userId = 0;
        List<Long> products = new ArrayList<>();
        String payment = "";
    }
}
