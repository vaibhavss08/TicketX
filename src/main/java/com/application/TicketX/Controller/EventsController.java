package com.application.TicketX.Controller;

import com.application.TicketX.Model.EventsModel;
import com.application.TicketX.Service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {
    @Autowired
    private EventsService eventsService;

    @GetMapping("/getAllEvents")
    public List<EventsModel> getAllEvents(){
        return eventsService.getAllEvents();
    }

    @GetMapping("/getEventTicketsById")
    public Integer findEventTicketsByEventId(@RequestParam String eventId){
        return eventsService.findEventTicketsByEventId(eventId);
    }
}
