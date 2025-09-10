package com.example.moviewishlist.unit;

import com.example.moviewishlist.model.Model;
import com.example.moviewishlist.repository.Repository;
import com.example.moviewishlist.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MovieServiceTest {

    private Repository repository;
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        repository = mock(Repository.class);
        movieService = new MovieService(repository);
    }

    @Test
    void testAddMovie() {
        Model movie = new Model("Inception", "Mind-bending thriller", List.of("Sci-Fi"), false);
        when(repository.save(movie)).thenReturn(movie);

        Model result = movieService.addMovie(movie);

        assertEquals("Inception", result.getTitle());
        assertFalse(result.isDone());
        verify(repository).save(movie);
    }

    @Test
    void testGetAllMovies() {
        Model movie1 = new Model("Inception", "Thriller", List.of("Sci-Fi"), false);
        Model movie2 = new Model("Interstellar", "Space epic", List.of("Sci-Fi"), true);
        when(repository.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        List<Model> movies = movieService.getAllMovies();

        assertEquals(2, movies.size());
        assertEquals("Interstellar", movies.get(1).getTitle());
        verify(repository).findAll();
    }

    @Test
    void testGetMovieById() {
        Model movie = new Model("Inception", "Thriller", List.of("Sci-Fi"), false);
        movie.setId("1");
        when(repository.findById("1")).thenReturn(Optional.of(movie));

        Optional<Model> result = movieService.getMovieById("1");

        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
        verify(repository).findById("1");
    }

    @Test
    void testUpdateMovie() {
        Model existing = new Model("Old Title", "Old Desc", List.of("Drama"), false);
        existing.setId("1");

        Model updated = new Model("New Title", "New Desc", List.of("Action"), true);

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(Model.class))).thenReturn(existing); // returns updated existing

        Model result = movieService.updateMovie("1", updated);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Desc", result.getDescription());
        assertEquals(List.of("Action"), result.getTags());
        assertTrue(result.isDone());
        verify(repository).save(existing);
    }

    @Test
    void testDeleteMovie() {
        movieService.deleteMovie("1");
        verify(repository).deleteById("1");
    }
}
