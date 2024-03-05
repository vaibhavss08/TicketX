package com.application.TicketX.Controller;

import com.application.TicketX.Entity.Ticket;
import com.application.TicketX.Model.TicketsModel;
import com.application.TicketX.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/getTicketById")
    public Ticket getTicketById(@RequestParam String ticketId){
        return ticketService.getTicketById(ticketId);
    }

    @GetMapping("/getTicketByUserId")
    public List<TicketsModel> getTicketByUserId(@RequestParam String userId){
        return ticketService.getTicketByUserId(userId);
    }
    @GetMapping("/buyTicket")
    public List<Ticket> buyTicketByEventId(@RequestParam String userId, @RequestParam String eventId,@RequestParam Integer numberOfTickets){
        return ticketService.buyTicketByEventId(userId, eventId, numberOfTickets);
    }

    @GetMapping("/sellTicket")
    public List<Ticket> sellTicketsByEventId(@RequestParam String userId, @RequestParam String eventId,@RequestParam Integer numberOfTickets){
        return ticketService.sellTicketsByEventId(userId, eventId, numberOfTickets);
    }
}
