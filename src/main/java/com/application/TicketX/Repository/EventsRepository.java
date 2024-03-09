package com.application.TicketX.Repository;

import com.application.TicketX.Model.EventsModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepository{
    List<EventsModel> findEvents ();
    Integer findEventTicketsByEventId(String eventId);
}
