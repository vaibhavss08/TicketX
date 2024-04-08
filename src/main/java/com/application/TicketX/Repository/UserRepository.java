package com.application.TicketX.Repository;

import com.application.TicketX.Entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    Boolean registerUser(String userId,String name, String mail);
    User userDetails(String userId);
}
