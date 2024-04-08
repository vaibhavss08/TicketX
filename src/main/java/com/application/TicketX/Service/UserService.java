package com.application.TicketX.Service;

import com.application.TicketX.Entity.User;
import com.application.TicketX.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public Boolean registerUser(String userId, String name, String mail) {
        return userRepository.registerUser(userId, name, mail);
    }
    public User userDetails(String userId){
        return userRepository.userDetails(userId);
    }

}
