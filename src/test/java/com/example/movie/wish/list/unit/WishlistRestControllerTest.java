package com.example.movie.wish.list.unit;

import com.example.movie.wish.list.controller.WishlistRestController;
import com.example.movie.wish.list.dto.WishlistDTO;
import com.example.movie.wish.list.model.Wishlist;
import com.example.movie.wish.list.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistRestControllerTest {

    @Mock
    private WishlistService service;

    @InjectMocks
    private WishlistRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMovie() {
        // given - ✅ DTO for input (not Wishlist!)
        WishlistDTO dto = new WishlistDTO("Inception", "Sci-Fi");
        
        // expected entity returned by service
        Wishlist expectedMovie = new Wishlist("Inception", "Sci-Fi");
        
        when(service.addMovie(any(Wishlist.class))).thenReturn(expectedMovie);

        // when - ✅ pass DTO to controller
        Wishlist result = controller.createMovie(dto);

        // then
        assertEquals("Inception", result.getTitle());
        assertEquals("Sci-Fi", result.getDescription());
        verify(service, times(1)).addMovie(any(Wishlist.class));
    }

    @Test
    void testGetAllMovies() {
        Wishlist movie1 = new Wishlist("Inception", "Sci-Fi");
        Wishlist movie2 = new Wishlist("Matrix", "Action");
        when(service.getAllMovies()).thenReturn(Arrays.asList(movie1, movie2));

        List<Wishlist> movies = controller.getAllMovies();
        assertEquals(2, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
        assertEquals("Matrix", movies.get(1).getTitle());
        verify(service, times(1)).getAllMovies();
    }

    @Test
    void testGetMovieById_Found() {
        Wishlist movie = new Wishlist("Inception", "Sci-Fi");
        when(service.getMovieById("1")).thenReturn(Optional.of(movie));

        Wishlist result = controller.getMovieById("1");
        assertEquals("Inception", result.getTitle());
        verify(service, times(1)).getMovieById("1");
    }

    @Test
    void testGetMovieById_NotFound() {
        when(service.getMovieById("99")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.getMovieById("99"));
        assertEquals("Movie not found", ex.getMessage());
        verify(service, times(1)).getMovieById("99");
    }

    @Test
    void testUpdateMovie() {
        // given - ✅ DTO for input (not Wishlist!)
        WishlistDTO dto = new WishlistDTO("Inception Reloaded", "Mind-bender");
        
        // expected entity returned by service
        Wishlist updatedMovie = new Wishlist("Inception Reloaded", "Mind-bender");
        
        when(service.updateMovie(eq("1"), any(Wishlist.class))).thenReturn(updatedMovie);

        // when - ✅ pass DTO to controller
        Wishlist result = controller.updateMovie("1", dto);

        // then
        assertEquals("Inception Reloaded", result.getTitle());
        assertEquals("Mind-bender", result.getDescription());
        verify(service, times(1)).updateMovie(eq("1"), any(Wishlist.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void testDeleteMovie() {
        doNothing().when(service).deleteMovie("1");

        assertDoesNotThrow(() -> controller.deleteMovie("1"));

        verify(service, times(1)).deleteMovie("1");
        verifyNoMoreInteractions(service);
    }
}
