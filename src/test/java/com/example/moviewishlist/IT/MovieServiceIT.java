package com.example.moviewishlist.IT;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MongoDBContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection; // Boot 3.1+

@Testcontainers
@SpringBootTest // use RANDOM_PORT if you actually start the web server here
class MovieServiceIT {

    @Container
    @ServiceConnection // <-- Boot will auto-wire spring.data.mongodb.uri for you
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7");

    @Test
    void contextLoads() {
        // your ITs...
    }
}
