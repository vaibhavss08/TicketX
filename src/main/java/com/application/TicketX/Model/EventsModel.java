package com.application.TicketX.Model;

import lombok.Data;

@Data
public class EventsModel {
    private String eventId;
    private String eventName;
    private String eventDate;
    private String eventLocation;
    private String eventDescription;
    private Integer numberOfAvailableTickets;
}
