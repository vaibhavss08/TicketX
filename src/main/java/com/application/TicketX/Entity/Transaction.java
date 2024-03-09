package com.application.TicketX.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Transactions")
public class Transaction{
    @Id
    private String transactionId;
    private String sellerUserId;
    private String buyerUserId;
    private String eventId;
    private String ticketId;
    private Date lastUpdatedAt;
}
