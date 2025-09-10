package com.example.moviewishlist.unit;

import com.example.moviewishlist.controller.WishlistRestController;
import com.example.moviewishlist.controller.WishlistRestController;
import com.example.moviewishlist.model.Model;
import com.example.moviewishlist.*;
import com.example.moviewishlist.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest

class WishlistRestControllerTest {

    private MovieService MovieService;
    private WishlistRestController controller;

    @BeforeEach
    void setUp() {
        MovieService = mock(MovieService.class);
        controller = new WishlistRestController(MovieService);
    }

    @Test
    void testCreateMovie() {
        Model Model = new Model("Inception", "Sci-Fi");
        when(MovieService.addMovie(any())).thenReturn(Model);

        Model result = controller.createMovie(Model);
        assertEquals("Inception", result.getTitle());
        assertEquals("Sci-Fi", result.getDescription());
    }

    @Test
    void testGetAllMovies() {
        Model movie1 = new Model("Inception", "Sci-Fi");
        Model movie2 = new Model("Matrix", "Action");

        when(MovieService.getAllMovies()).thenReturn(Arrays.asList(movie1, movie2));

        List<Model> movies = controller.getAllMovies();
        assertEquals(2, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
        assertEquals("Matrix", movies.get(1).getTitle());
    }

    @Test
    void testGetMovieById() {
        Model Model = new Model("Inception", "Sci-Fi");
        when(MovieService.getMovieById("1")).thenReturn(Optional.of(Model));

        Model result = controller.getMovieById("1");
        assertEquals("Inception", result.getTitle());
    }

//    @Test
//    void testGetMovieById_NotFound() {
//        when(MovieService.getMovieById("99")).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            controller.getMovieById("99");
//        });
//
//        assertEquals("Movie not found", exception.getMessage());
//    }

    @Test
    void testUpdateMovie() {
        Model updated = new Model("Inception Reloaded", "Sci-Fi");
        when(MovieService.updateMovie(eq("1"), any())).thenReturn(updated);

        Model result = controller.updateMovie("1", updated);
        assertEquals("Inception Reloaded", result.getTitle());
    }

    @Test
    void testDeleteMovie() {
        doNothing().when(MovieService).deleteMovie("1");

        assertDoesNotThrow(() -> controller.deleteMovie("1"));
        verify(MovieService, times(1)).deleteMovie("1");
    }
}
