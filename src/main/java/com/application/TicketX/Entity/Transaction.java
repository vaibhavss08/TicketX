package com.application.TicketX.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Transactions")
public class Transaction {
    @Id
    String transactionId;
    String sellerUserId;
    String buyerUserId;
    String eventId;
    List<String> tickets;
    Date lastUpdatedAt;
    Integer numberOfTickets;
}
