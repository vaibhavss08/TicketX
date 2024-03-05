package com.application.TicketX.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Tickets")
public class Ticket {
    @Id
    private String ticketId;
    private String eventId;
    private String userId;
    private Integer status;
    private Date lastUpdatedAt;
}
