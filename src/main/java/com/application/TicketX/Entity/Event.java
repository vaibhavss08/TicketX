package com.application.TicketX.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Events")
public class Event {
    @Id
    private String eventId;
    private String name;
    private String date;
    private String eventDescription;
    private String location;
}
