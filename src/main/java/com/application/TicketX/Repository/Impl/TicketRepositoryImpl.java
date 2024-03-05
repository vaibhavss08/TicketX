package com.application.TicketX.Repository.Impl;

import com.application.TicketX.Entity.Ticket;
import com.application.TicketX.Model.TicketsModel;
import com.application.TicketX.Repository.TicketRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class TicketRepositoryImpl implements TicketRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoTransactionManager transactionManager;

    @Override
    public Ticket findTicketByTicketId(String ticketId) {
        MatchOperation matchStage = match(new Criteria("_id").is(ticketId));
        Aggregation aggregation = newAggregation(matchStage);
        AggregationResults<Ticket> results = mongoTemplate.aggregate(aggregation, "Tickets", Ticket.class);
        return results.getMappedResults().get(0);
    }

    @Override
    public List<TicketsModel> findTicketByUserId(String userId) {
        Criteria criteria1 = Criteria.where("userId").is(userId);
        Criteria criteria2 = Criteria.where("status").is(2);
        Criteria combinedCriteria = new Criteria().andOperator(criteria1, criteria2);

        MatchOperation matchStage = match(combinedCriteria);
        LookupOperation lookupStage = lookup("Events","eventId","_id","eventDetails");
        AddFieldsOperation addFieldsStage = AddFieldsOperation
                .addField("eventName").withValue(new Document("$arrayElemAt", Arrays.asList("$eventDetails.name", 0)))
                .addField("eventDate").withValue(new Document("$arrayElemAt", Arrays.asList("$eventDetails.date", 0)))
                .addField("eventLocation").withValue(new Document("$arrayElemAt", Arrays.asList("$eventDetails.location", 0)))
                .addField("eventDescription").withValue(new Document("$arrayElemAt", Arrays.asList("$eventDetails.eventDescription", 0)))
                .build();
        GroupOperation groupStage = group("$eventId")
                .first("$eventName").as("eventName")
                .first("$eventDate").as("eventDate")
                .first("$eventLocation").as("eventLocation")
                .first("$eventId").as("eventId")
                .first("$userId").as("userId")
                .first("$eventDescription").as("eventDescription")
                .addToSet("$_id").as("tickets")
                .count().as("count");

        Aggregation aggregation = newAggregation(matchStage, lookupStage, addFieldsStage,groupStage);

        AggregationResults<TicketsModel> results = mongoTemplate.aggregate(aggregation, "Tickets", TicketsModel.class);
        return results.getMappedResults();
    }

    @Override
    public List<Ticket> buyTicketByEventId(String userId, String eventId, Integer numberOfTickets) {
        Query query = new Query(Criteria.where("eventId").is(eventId).and("status").is(1))
                .with(Sort.by(Sort.Direction.ASC, "lastUpdatedAt"))
                .limit(numberOfTickets);

        List<Ticket> results = mongoTemplate.find(query, Ticket.class);

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {
                    for (Ticket ticket : results) {
                        Query query = new Query(Criteria.where("_id").is(ticket.getTicketId()));
                        Update update = new Update()
                                .set("status", 2)
                                .set("userId", userId)
                                .set("lastUpdatedAt", Instant.now());
                        mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Ticket.class);
                    }
                } catch (Exception e) {
                    status.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });
        return results;
    }

    @Override
    public List<Ticket> sellTicketsByEventId(String userId, String eventId, Integer numberOfTickets) {
        Query query = new Query(Criteria.where("userId").is(userId).and("eventId").is(eventId).and("status").is(2))
                .with(Sort.by(Sort.Direction.ASC, "lastUpdatedAt"))
                .limit(numberOfTickets);

        List<Ticket> results = mongoTemplate.find(query, Ticket.class);

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {
                    for (Ticket ticket : results) {
                        Query query = new Query(Criteria.where("_id").is(ticket.getTicketId()));
                        Update update = new Update()
                                .set("status",1)
                                .set("lastUpdatedAt", Instant.now());
                        mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Ticket.class);
                    }
                } catch (Exception e) {
                    status.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });
        return results;
    }
}
