package com.example.movie.wish.list.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.movie.wish.list.model.Wishlist;


public interface WishlistRepository extends MongoRepository<Wishlist, String> { }