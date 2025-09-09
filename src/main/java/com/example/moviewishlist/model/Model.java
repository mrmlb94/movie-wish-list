package com.example.moviewishlist.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.ArrayList;

@Document(collection = "moviesCollection")
public class Model{
    @Id
    private String id;
    private String title;
    private String description;
    private List<String> tags;
    private boolean done;

    public Model() {
        this.tags = new ArrayList<>();
        this.done = false;
    }

    public Model(String title) {
        this();
        this.title = title;
    }

    public Model(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public Model(String title, String description, List<String> tags, boolean done) {
        this.title = title;
        this.description = description;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.done = done;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public void setDone(Boolean done) { this.done = done != null ? done : false; }
}

//What You Should Test in a Model Class :
    //Constructor behavior (e.g., default values like done = false)
    //Getter/setter correctness
    //Edge cases (e.g., null tags list)
    //Equality/hashCode if overridden
    //Serialization (optional)
