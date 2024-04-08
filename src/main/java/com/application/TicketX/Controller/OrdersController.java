package com.application.TicketX.Controller;

import com.application.TicketX.Model.TicketsModel;
import com.application.TicketX.Service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders/")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @PutMapping("/buyTicket")
    public Boolean buyTicketByEventId(@RequestParam String userId, @RequestParam String eventId, @RequestParam Integer numberOfTickets){
        return ordersService.buyTicketByEventId(userId, eventId, numberOfTickets);
    }
    @PutMapping("/sellTicket")
    public Boolean sellTicketsByEventId(@RequestParam String userId, @RequestParam String eventId,@RequestParam Integer numberOfTickets){
        return ordersService.sellTicketsByEventId(userId, eventId, numberOfTickets);
    }

    @PutMapping("/cancelOrder")
    public Boolean cancelSellOrder(@RequestParam String userId, @RequestParam String eventId){
        return ordersService.cancelSellOrder(userId, eventId);
    }

    @PutMapping("/modifyOrder")
    public Boolean modifySellOrder(@RequestParam String userId, @RequestParam String eventId, @RequestParam Integer numberOfTickets) {
        return ordersService.modifySellOrder(userId, eventId, numberOfTickets);
    }

    @GetMapping("/viewPendingOrder")
    public List<TicketsModel> viewPendingOrder(@RequestParam String userId){
        return ordersService.viewPendingOrder(userId);
    }


}
