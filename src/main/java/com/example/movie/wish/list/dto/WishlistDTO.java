package com.example.movie.wish.list.dto;

import java.util.List;

/**
 * Data Transfer Object for Wishlist API requests.
 * Prevents exposing persistent entity directly to external clients,
 * addressing SonarCloud security vulnerability.
 */
public class WishlistDTO {
    private String title;
    private String description;
    private List<String> tags;
    private boolean done;

    // ✅ Default constructor (required for Jackson deserialization)
    public WishlistDTO() {}

    // ✅ Constructor with title and description only
    public WishlistDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // ✅ Constructor with all fields
    public WishlistDTO(String title, String description, List<String> tags, boolean done) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.done = done;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
