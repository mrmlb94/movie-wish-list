package com.example.movie.wish.list.service;

import com.example.movie.wish.list.model.Wishlist;
import com.example.movie.wish.list.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    private final WishlistRepository repository;

    public WishlistService(WishlistRepository repository) {
        this.repository = repository;
    }

    // Create
    public Wishlist addMovie(Wishlist wishlist) {
        return repository.save(wishlist);
    }

    // Read
    public List<Wishlist> getAllMovies() {
        return repository.findAll();
    }

    public Optional<Wishlist> getMovieById(String id) {
        return repository.findById(id);
    }

    // Update
    public Wishlist updateMovie(String id, Wishlist updatedMovie) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setTitle(updatedMovie.getTitle());
                    existing.setDescription(updatedMovie.getDescription());
                    existing.setTags(updatedMovie.getTags());
                    existing.setDone(updatedMovie.isDone());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    // Delete
    public void deleteMovie(String id) {
        repository.deleteById(id);
    }
}
