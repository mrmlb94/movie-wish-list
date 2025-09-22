package com.example.movie.wish.list;

import com.example.movie.wish.list.model.Wishlist;
import com.example.movie.wish.list.repository.WishlistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class MovieWishListApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieWishListApplication.class, args);
        System.out.println("The app is running");
    }

    @Bean
    CommandLineRunner initDatabase(WishlistRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Wishlist[] movies = new Wishlist[] {
                    new Wishlist("Inception", "A mind-bending thriller", Arrays.asList("Sci-Fi"), false),
//                    new Wishlist("The Matrix", "Neo discovers the truth", Arrays.asList("Sci-Fi", "Action"), true),
//                    new Wishlist("Interstellar", "A journey through space", Arrays.asList("Sci-Fi", "Drama"), false),
//                    new Wishlist("The Godfather", "The story of a crime family", Arrays.asList("Crime", "Drama"), false),
//                    new Wishlist("The Dark Knight", "Batman faces the Joker", Arrays.asList("Action", "Drama"), true),
//                    new Wishlist("Pulp Fiction", "Intertwined criminal stories", Arrays.asList("Crime", "Drama"), false),
//                    new Wishlist("Fight Club", "An underground fight club", Arrays.asList("Drama", "Thriller"), false),
//                    new Wishlist("Forrest Gump", "Life story of Forrest", Arrays.asList("Drama", "Comedy"), true),
//                    new Wishlist("The Lord of the Rings: The Fellowship of the Ring", "Epic fantasy journey", Arrays.asList("Fantasy", "Adventure"), false),
//                    new Wishlist("Star Wars: Episode IV - A New Hope", "Rebels vs Empire", Arrays.asList("Sci-Fi", "Adventure"), false),
//                    new Wishlist("The Shawshank Redemption", "Friendship and hope in prison", Arrays.asList("Drama"), true),
//                    new Wishlist("The Avengers", "Earth's mightiest heroes assemble", Arrays.asList("Action", "Sci-Fi"), true)
                };
                repository.saveAll(Arrays.asList(movies));
                System.out.println("12 popular movies added to the database!");
            }
        };
    }
}
