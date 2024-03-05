package com.application.TicketX.Service;

import com.application.TicketX.Entity.Event;
import com.application.TicketX.Model.EventsModel;
import com.application.TicketX.Repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventsService {
    @Autowired
    private EventsRepository eventsRepository;

    public List<EventsModel> getAllEvents(){
        return eventsRepository.findEvents();
    }
}
