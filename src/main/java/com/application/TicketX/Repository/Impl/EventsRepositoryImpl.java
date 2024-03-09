package com.application.TicketX.Repository.Impl;

import com.application.TicketX.Entity.Ticket;
import com.application.TicketX.Model.EventsModel;
import com.application.TicketX.Repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class EventsRepositoryImpl implements EventsRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<EventsModel> findEvents() {
        LookupOperation lookupStage = lookup("Tickets","_id","eventId","ticketDetails");
        UnwindOperation unwindStage = unwind("ticketDetails");
        MatchOperation matchStage = match(new Criteria("ticketDetails.status").is(1));
        GroupOperation groupStage = group("_id")
                .first("$_id").as("eventId")
                .first("$name").as("eventName")
                .first("$date").as("eventDate")
                .first("$location").as("eventLocation")
                .first("$eventDescription").as("eventDescription")
                .count().as("numberOfAvailableTickets");
        Aggregation aggregation = newAggregation(lookupStage, unwindStage, matchStage, groupStage);
        AggregationResults<EventsModel> results = mongoTemplate.aggregate(aggregation, "Events", EventsModel.class);
        return results.getMappedResults();
    }

    @Override
    public Integer findEventTicketsByEventId(String eventId) {
        Query query = new Query(Criteria.where("eventId").is(eventId).and("status").is(1));
        return Math.toIntExact(mongoTemplate.count(query, Ticket.class));
    }


}
