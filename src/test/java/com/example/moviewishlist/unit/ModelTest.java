package com.example.moviewishlist.unit;

import com.example.moviewishlist.model.Model;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void defaultConstructorShouldInitializeTagsAndDone() {
        Model model = new Model();
        assertNotNull(model.getTags());
        assertFalse(model.isDone());
    }

    @Test
    void constructorWithTitleShouldSetTitle() {
        Model model = new Model("Inception");
        assertEquals("Inception", model.getTitle());
        assertFalse(model.isDone());
    }

    @Test
    void constructorWithTitleAndDescriptionShouldSetBoth() {
        Model model = new Model("Inception", "A mind-bending thriller");
        assertEquals("Inception", model.getTitle());
        assertEquals("A mind-bending thriller", model.getDescription());
    }

    @Test
    void fullConstructorShouldSetAllFields() {
        Model model = new Model("Inception", "A mind-bending thriller", Arrays.asList("Sci-Fi", "Thriller"), true);
        assertEquals("Inception", model.getTitle());
        assertEquals("A mind-bending thriller", model.getDescription());
        assertEquals(Arrays.asList("Sci-Fi", "Thriller"), model.getTags());
        assertTrue(model.isDone());
    }

    @Test
    void setDoneWithNullShouldDefaultToFalse() {
        Model model = new Model();
        model.setDone(null);
        assertFalse(model.isDone());
    }

}
