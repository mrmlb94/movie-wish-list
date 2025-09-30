package com.example.movie.wish.list.it;

import com.example.movie.wish.list.TestcontainersConfiguration;
import com.example.movie.wish.list.model.Wishlist;
import com.example.movie.wish.list.repository.WishlistRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class) // activate Mongo container
class WishlistRepositoryIT {

    @Autowired
    private WishlistRepository repository;
    @BeforeEach
    void cleanDb() {
        repository.deleteAll(); // ✅ ensure a clean state
    }

    @Test
    void testInsertAndFindMovie() {
        Wishlist movie = new Wishlist("Tenet", "Time inversion thriller");
        repository.save(movie);

        List<Wishlist> allMovies = repository.findAll();

        assertThat(allMovies).isNotEmpty();
        assertThat(allMovies.getFirst().getTitle()).isEqualTo("Tenet");
    }
}
//This test will:
//Spin up a real MongoDB container (via Testcontainers).
//Connect Spring Boot automatically using @ServiceConnection.
//Run against the real DB.
//Works on Mac, Linux, Windows, GitHub Actions.