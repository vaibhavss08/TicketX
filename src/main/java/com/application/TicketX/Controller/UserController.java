package com.application.TicketX.Controller;

import com.application.TicketX.Entity.User;
import com.application.TicketX.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registerUser")
    public Boolean registerUser(String userId, String name, String mail) {
        return userService.registerUser(userId, name, mail);
    }
    @GetMapping("/userDetails")
    public User userDetails(String userId){
        return userService.userDetails(userId);
    }
}
