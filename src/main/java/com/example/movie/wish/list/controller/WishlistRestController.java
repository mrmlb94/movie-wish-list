package com.example.movie.wish.list.controller;

import com.example.movie.wish.list.model.Wishlist;
import com.example.movie.wish.list.service.WishlistService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistRestController {

    private final WishlistService service;

    public WishlistRestController(WishlistService service) {
        this.service = service;
    }

    @PostMapping
    public Wishlist createMovie(@RequestBody Wishlist wishlist) {
        return service.addMovie(wishlist);
    }

    @GetMapping
    public List<Wishlist> getAllMovies() {
        return service.getAllMovies();
    }

    @GetMapping("/{id}")
    public Wishlist getMovieById(@PathVariable String id) {
        return service.getMovieById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    @PutMapping("/{id}")
    public Wishlist updateMovie(@PathVariable String id, @RequestBody Wishlist updatedMovie) {
        return service.updateMovie(id, updatedMovie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable String id) {
        service.deleteMovie(id);
    }

}
