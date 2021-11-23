package de.haevn.distributed_systems.v2.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "users")
public class User {
    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private Long id = -1L;
    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String password;

}
