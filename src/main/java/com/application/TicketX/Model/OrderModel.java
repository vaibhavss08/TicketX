package com.application.TicketX.Model;

import lombok.Data;

@Data
public class OrderModel {
    private String userId;
    private String eventId;
    private String eventName;
    private String eventDate;
    private Integer pendingTickets;
}
