package com.application.TicketX.Repository.Impl;

import com.application.TicketX.Entity.User;
import com.application.TicketX.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Boolean registerUser(String userId, String name, String mail) {
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setMail(mail);
        try {
            mongoTemplate.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User userDetails(String userId) {
        return mongoTemplate.findOne(new Query(Criteria.where("_id").is(userId)), User.class);
    }
}
