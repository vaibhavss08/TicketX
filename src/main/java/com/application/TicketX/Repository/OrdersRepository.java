package com.application.TicketX.Repository;

import com.application.TicketX.Entity.Transaction;
import com.application.TicketX.Model.TicketsModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository {
    Boolean buyTicketByEventId(String userId, String eventId, Integer numberOfTickets);
    Boolean sellTicketsByEventId(String userId, String eventId, Integer numberOfTickets);
    List<Transaction> viewSoldTickets(String userId);
    List<TicketsModel> viewPendingOrder(String userId);
    Boolean modifySellOrder(String userId, String eventId, Integer numberOfTickets);
    Boolean cancelSellOrder(String userId, String eventId);
}
