package de.haevn.distributed_systems.v2.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "products")
public class Product {
    @Transient
    public static final String SEQUENCE_NAME = "product_sequence";

    @Id
    private Long id;
    private String name;
    private String brand;
    private Double newPrice;
    private Double oldPrice;
}
