package com.application.TicketX.Model;

import lombok.Data;

import java.util.List;

@Data
public class TicketsModel {
    private String eventName;
    private String eventDate;
    private String eventLocation;
    private String eventDescription;
    private List<String> tickets;
    private Integer count;
    private String userId;
    private String eventId;
}
