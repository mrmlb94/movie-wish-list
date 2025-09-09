package com.example.moviewishlist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Map<String, Object> hello(@RequestParam(value = "name", required = false) String name) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Instant.now().toString());
        response.put("status", "success");

        if (name != null && !name.isEmpty()) {
            response.put("message", "Hello, " + name + "! Welcome to Movie Wishlist!");
            response.put("movieCount", 0);
        } else {
            response.put("message", "Welcome to Movie Wishlist! You have 0 movies in your wishlist.");
            response.put("movieCount", 0);
        }

        return response;
    }
}
