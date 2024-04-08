package com.application.TicketX.Service;

import com.application.TicketX.Model.TicketsModel;
import com.application.TicketX.Repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    public Boolean buyTicketByEventId(String userId, String eventId, Integer numberOfTickets) {
        return ordersRepository.buyTicketByEventId(userId, eventId,numberOfTickets);
    }

    public Boolean sellTicketsByEventId(String userId, String eventId, Integer numberOfTickets) {
        return ordersRepository.sellTicketsByEventId(userId, eventId, numberOfTickets);
    }
    public Boolean modifySellOrder(String userId, String eventId, Integer numberOfTickets) {
        return ordersRepository.modifySellOrder(userId, eventId, numberOfTickets);
    }
    public Boolean cancelSellOrder(String userId, String eventId) {
        return ordersRepository.cancelSellOrder(userId, eventId);
    }

    public List<TicketsModel> viewPendingOrder(String userId){
        return ordersRepository.viewPendingOrder(userId);
    }

}
