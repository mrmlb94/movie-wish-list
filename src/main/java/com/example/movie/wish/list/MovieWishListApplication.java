package com.example.movie.wish.list;

import com.example.movie.wish.list.model.Wishlist;
import com.example.movie.wish.list.repository.WishlistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class MovieWishListApplication {
    private static final Logger logger = LoggerFactory.getLogger(MovieWishListApplication.class);

    public static void main(String[] args) {


        SpringApplication.run(MovieWishListApplication.class, args);
        logger.info("The app is running");
    }

    @Bean
    CommandLineRunner initDatabase(WishlistRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Wishlist[] movies = new Wishlist[] {
                    new Wishlist("Inception", "A mind-bending thriller", Collections.singletonList("Sci-Fi"), false),
                };
                repository.saveAll(Arrays.asList(movies));
                logger.info("1 popular movie added to the database!");
            }
        };
    }
}
