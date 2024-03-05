package com.application.TicketX;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Bean
    public MongoTransactionManager transactionManager() {
        return new MongoTransactionManager(mongoTemplate.getMongoDatabaseFactory());
    }
}
