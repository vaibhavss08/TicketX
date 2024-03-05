package com.application.TicketX.Repository;

import com.application.TicketX.Entity.Ticket;
import com.application.TicketX.Model.TicketsModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository {
    Ticket findTicketByTicketId(String ticketId);
    List<TicketsModel> findTicketByUserId(String userId);
    List<Ticket> buyTicketByEventId(String userId, String eventId, Integer numberOfTickets);
    List<Ticket> sellTicketsByEventId(String userId, String eventId, Integer numberOfTickets);
}
