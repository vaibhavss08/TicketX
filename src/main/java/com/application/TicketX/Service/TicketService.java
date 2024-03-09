package com.application.TicketX.Service;

import com.application.TicketX.Entity.Ticket;
import com.application.TicketX.Model.TicketsModel;
import com.application.TicketX.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public Ticket getTicketById(String ticketId) {
        return ticketRepository.findTicketByTicketId(ticketId);
    }

    public List<TicketsModel> getTicketByUserId(String userId) {
        return ticketRepository.findTicketByUserId(userId);
    }

    public Integer getTicketCountByUserIdEventId(String userId, String eventId) {
        return ticketRepository.getTicketCountByUserIdEventId(userId, eventId);
    }
}
