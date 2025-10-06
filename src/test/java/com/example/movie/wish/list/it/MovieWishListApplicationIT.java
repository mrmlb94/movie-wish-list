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
@Import(TestcontainersConfiguration.class) // start MongoDB container
class MovieWishListApplicationIT {

    @Autowired
    private WishlistRepository repository;

    
    
    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }
    
    
    @Test
    void contextLoads() {
        assertThat(repository).isNotNull();
    }

    @Test
    void testInsertAndRetrieveMovie() {
        // given
        Wishlist movie = new Wishlist("Inception", "Space exploration and time dilation");
        repository.save(movie);

        // when
        List<Wishlist> all = repository.findAll();

        // then
        assertThat(all).isNotEmpty();
        assertThat(all.get(0).getTitle()).isEqualTo("Inception");
    }
}



//✅ Uses @Import(TestcontainersConfiguration.class) → spins up a real MongoDB container.
//✅ Checks repository bean is injected (Spring Boot started correctly).
//✅ Inserts a movie and verifies retrieval from real MongoDB.
//✅ Renamed to *IT.java → picked up by Failsafe in mvn verify.