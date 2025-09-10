package com.example.moviewishlist.service;
import com.example.moviewishlist.model.Model;
import com.example.moviewishlist.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final Repository repository;

    public MovieService(Repository repository) {
        this.repository = repository;
    }

    // Create
    public Model addMovie(Model wishlist) {
        return repository.save(wishlist);
    }

    // Read
    public List<Model> getAllMovies() {
        return repository.findAll();
    }

    public Optional<Model> getMovieById(String id) {
        return repository.findById(id);
    }

    // Update
    public Model updateMovie(String id, Model updatedMovie) {
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
