package com.application.TicketX.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Users")
public class User {
    @Id
    private String userId;
    private String name;
    private String mail;
}
