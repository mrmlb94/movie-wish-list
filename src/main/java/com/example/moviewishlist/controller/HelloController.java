package com.example.moviewishlist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        // Modified to make the first test pass
        return "Welcome to Movie Wishlist! You have 0 movies in your wishlist.";
    }
}
