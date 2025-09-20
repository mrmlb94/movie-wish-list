package com.example.movie.wish.list.it;

import com.example.movie.wish.list.TestcontainersConfiguration;
import com.example.movie.wish.list.model.Wishlist;
import com.example.movie.wish.list.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class WishlistRestControllerIT {
    @Container
    static MongoDBContainer mongoDB = new MongoDBContainer("mongo:latest");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WishlistRepository repository;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll(); // ✅ clear DB before each test
    }

    @Test
    void testAddAndGetWishlist() {
        // given
        String url = "http://localhost:" + port + "/api/wishlist";
        Wishlist newMovie = new Wishlist("Inception", "Dream within a dream");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Wishlist> request = new HttpEntity<>(newMovie, headers);

        // when → POST new movie
        ResponseEntity<Wishlist> response = restTemplate.postForEntity(url, request, Wishlist.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Inception");

        // when → GET all movies
        ResponseEntity<Wishlist[]> getResponse = restTemplate.getForEntity(url, Wishlist[].class);

        // then
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotEmpty();

        // ✅ flatten array into list correctly
        List<Wishlist> allMovies = Arrays.asList(getResponse.getBody());
        assertThat(allMovies.stream().map(Wishlist::getTitle))
                .contains("Inception");
    }
}

//🔹 Key Points:
//@SpringBootTest(webEnvironment = RANDOM_PORT) → boots full Spring Boot app with web server.
//@Import(TestcontainersConfiguration.class) → ensures MongoDB container is available.
//TestRestTemplate → performs real HTTP calls against your controller.
//Covers POST + GET endpoints.
//Ensures your WishlistRestController is truly working with a real Mongo.

//MovieWishListApplicationIT → tests repository layer.
//WishlistRestControllerIT → tests REST API end-to-end.

//This setup is best practice for Spring Boot:
//Unit tests = mocks (fast, no DB).
//Integration tests = Testcontainers (real Mongo).
//Controlled entirely via pom.xml + Maven lifecycle (no .sh).
