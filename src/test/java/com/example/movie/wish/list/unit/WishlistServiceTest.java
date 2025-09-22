package com.example.movie.wish.list.unit;

import com.example.movie.wish.list.model.Wishlist;
import com.example.movie.wish.list.repository.WishlistRepository;
import com.example.movie.wish.list.service.WishlistService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    private WishlistRepository repository;
    private WishlistService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(WishlistRepository.class);
        service = new WishlistService(repository);
    }

    // ✅ Create
    @Test
    void testAddMovie() {
        Wishlist movie = new Wishlist("Inception", "Sci-Fi");
        when(repository.save(any(Wishlist.class))).thenReturn(movie);

        Wishlist saved = service.addMovie(movie);

        // Stronger assertion - same instance (kills mutants returning a new instance or null)
        assertThat(saved).isSameAs(movie);
        verify(repository, times(1)).save(movie);
        verifyNoMoreInteractions(repository);
    }

    // ✅ Update (kills VOID_METHOD_CALLS on setTitle/setDescription/setTags/setDone)
    @Test
    void testUpdateMovie() {
        // Given: existing entity in repo
        Wishlist existing = new Wishlist("Inception", "Sci-Fi");
        existing.setTags(List.of("classic"));
        existing.setDone(false);

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(Wishlist.class))).thenAnswer(inv -> inv.getArgument(0));

        // When: updating multiple fields, incl. tags + done + description
        Wishlist updatedMovie = new Wishlist("Inception Reloaded", "Neo Sci-Fi"); // CHANGED
        updatedMovie.setTags(Arrays.asList("sci-fi", "thriller"));
        updatedMovie.setDone(true);

        Wishlist result = service.updateMovie("1", updatedMovie);

        // Then: capture what was actually persisted
        ArgumentCaptor<Wishlist> captor = ArgumentCaptor.forClass(Wishlist.class);
        verify(repository).save(captor.capture());
        Wishlist saved = captor.getValue();

        // These assertions will fail if any setter call is removed by a mutant
        assertThat(saved.getTitle()).isEqualTo("Inception Reloaded");
        assertThat(saved.getDescription()).isEqualTo("Neo Sci-Fi"); // CHANGED
        assertThat(saved.getTags()).containsExactly("sci-fi", "thriller");
        assertThat(saved.isDone()).isTrue();

        // Also assert the service's return mirrors the saved entity
        assertThat(result.getTitle()).isEqualTo("Inception Reloaded");
        assertThat(result.getDescription()).isEqualTo("Neo Sci-Fi"); // CHANGED
        assertThat(result.getTags()).containsExactly("sci-fi", "thriller");
        assertThat(result.isDone()).isTrue();

        verify(repository, times(1)).findById("1");
        verifyNoMoreInteractions(repository);
    }

    // ✅ Read - Get All
    @Test
    void testGetAllMovies() {
        Wishlist movie1 = new Wishlist("Inception", "Sci-Fi");
        Wishlist movie2 = new Wishlist("Matrix", "Action");
        when(repository.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        List<Wishlist> movies = service.getAllMovies();

        assertThat(movies).hasSize(2);
        assertThat(movies).containsExactly(movie1, movie2);
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    // ✅ Read - Get by ID
    @Test
    void testGetMovieById() {
        Wishlist movie = new Wishlist("Inception", "Sci-Fi");
        when(repository.findById("1")).thenReturn(Optional.of(movie));

        Optional<Wishlist> found = service.getMovieById("1");

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(movie);
        verify(repository, times(1)).findById("1");
        verifyNoMoreInteractions(repository);
    }

    // ✅ Read - Get by ID not found
    @Test
    void testGetMovieById_NotFound() {
        when(repository.findById("99")).thenReturn(Optional.empty());

        Optional<Wishlist> found = service.getMovieById("99");

        assertThat(found).isEmpty();
        verify(repository, times(1)).findById("99");
        verifyNoMoreInteractions(repository);
    }

    // ✅ Update - Not Found
    @Test
    void testUpdateMovie_NotFound() {
        when(repository.findById("99")).thenReturn(Optional.empty());
        Wishlist updatedMovie = new Wishlist("Inception Reloaded", "Sci-Fi");

        assertThatThrownBy(() -> service.updateMovie("99", updatedMovie))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Movie not found");

        verify(repository, times(1)).findById("99");
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    // ✅ Delete
    @Test
    void testDeleteMovie() {
        doNothing().when(repository).deleteById("1");

        service.deleteMovie("1");

        verify(repository, times(1)).deleteById("1");
        verifyNoMoreInteractions(repository);
    }
}
