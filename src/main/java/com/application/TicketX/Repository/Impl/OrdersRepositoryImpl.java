package com.application.TicketX.Repository.Impl;

import com.application.TicketX.Entity.Ticket;
import com.application.TicketX.Entity.Transaction;
import com.application.TicketX.Model.TicketsModel;
import com.application.TicketX.Repository.OrdersRepository;
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
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Repository
public class OrdersRepositoryImpl implements OrdersRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoTransactionManager transactionManager;
    @Override
    public Boolean buyTicketByEventId(String userId, String eventId, Integer numberOfTickets) {
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
                        Transaction transaction = new Transaction();
                        transaction.setTicketId(ticket.getTicketId());
                        transaction.setEventId(ticket.getEventId());
                        transaction.setBuyerUserId(userId);
                        transaction.setSellerUserId(ticket.getUserId());
                        transaction.setLastUpdatedAt(Date.from(Instant.now()));

                        Query query = new Query(Criteria.where("_id").is(ticket.getTicketId()));
                        Update update = new Update()
                                .set("status", 2)
                                .set("userId", userId)
                                .set("lastUpdatedAt", Instant.now());

                        mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Ticket.class);
                        mongoTemplate.save(transaction);
                    }
                } catch (Exception e) {
                    status.setRollbackOnly();
                    throw e;
                }
                return true;
            }
        });
        return false;
    }

    @Override
    public Boolean sellTicketsByEventId(String userId, String eventId, Integer numberOfTickets) {
        Query query1 = new Query(Criteria.where("userId").is(userId).and("eventId").is(eventId).and("status").is(2))
                .with(Sort.by(Sort.Direction.ASC, "lastUpdatedAt"));
        Integer totalAvailable = Math.toIntExact(mongoTemplate.count(query1, Ticket.class));

        if(numberOfTickets>totalAvailable){
            // Handle in UI if possible
            return false;
        }

        Query query = query1.limit(numberOfTickets);

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
                return true;
            }
        });
        return false;
    }
    @Override
    public List<TicketsModel> viewPendingOrder(String userId) {
        Criteria criteria1 = Criteria.where("userId").is(userId);
        Criteria criteria2 = Criteria.where("status").is(1);
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
    public Boolean modifySellOrder(String userId, String eventId, Integer numberOfTickets) {
        // numberOfTickets -> Net Amount of Tickets that need to be modified, calculation need to be handled in UI
        if(numberOfTickets == 0){
            return cancelSellOrder(userId, eventId);
        }

        Query query =  new Query(Criteria.where("userId").is(userId).and("eventId").is(eventId));
        Integer total = Math.toIntExact(mongoTemplate.count(query, Ticket.class));

        if(numberOfTickets > total){
            return false;
        }

        if(cancelSellOrder(userId, eventId)){
            return sellTicketsByEventId(userId, eventId, numberOfTickets);
        }

        return false;
    }

    @Override
    public Boolean cancelSellOrder(String userId, String eventId) {
        Query query = new Query(Criteria.where("userId").is(userId)
                .and("eventId").is(eventId)
                .and("status").is(1));

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
                                .set("lastUpdatedAt", Instant.now());
                        mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Ticket.class);
                    }
                } catch (Exception e) {
                    status.setRollbackOnly();
                    throw e;
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public List<Transaction> viewSoldTickets(String userId) {
        return null;
    }
}
