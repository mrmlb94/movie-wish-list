package com.example.moviewishlist.unit;
import com.example.moviewishlist.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnWelcomeMessageWithMovieCount() throws Exception {
        // This test will FAIL because the current implementation
        // doesn't return the expected message format

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Movie Wishlist! You have 0 movies in your wishlist."));
    }

    @Test
    void shouldReturnJsonResponse() throws Exception {
        // This test will FAIL because the current implementation
        // returns plain text, not JSON

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Hello, Movie Wishlist!"))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void shouldReturnCurrentTimestamp() throws Exception {
        // This test will FAIL because the current implementation
        // doesn't include timestamp information

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("Hello, Movie Wishlist!"));
    }

    @Test
    void shouldHandleHelloWithNameParameter() throws Exception {
        // This test will FAIL because the current implementation
        // doesn't accept or use a name parameter

        mockMvc.perform(get("/hello").param("name", "Reza"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Reza! Welcome to Movie Wishlist!"));
    }
}



// The warnings you're seeing are actually expected test failures
// this is the RED phase of TDD.
// what's happening:
//TDD RED Phase - Tests Are Failing As Expected!
//test results show:
//Test 1 Failed: Expected "Welcome to Movie Wishlist! You have 0 movies in your wishlist." but got "Hello, Movie Wishlist!"
//Test 2 Failed: Expected JSON content type but got text/plain;charset=UTF-8
//Test 3 Failed: Expected JSON with timestamp but got plain text
//Test 4 Failed: Expected personalized greeting but got static message
//This is EXACTLY what TDD wants!