package com.example.moviewishlist.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.moviewishlist.repository.Repository;
import com.example.moviewishlist.model.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataMongoTest
class MovieRepositoryTest {

    @Container
    static MongoDBContainer mongoContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0"))
            .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.host", mongoContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.database", () -> "testdb");
    }

    @Autowired
    private Repository movieRepository;

    @Test
    void shouldSaveAndFindMovie() {
        // Given
        Model movie = new Model("The Matrix", "A computer hacker learns the truth about reality");
        movie.setTags(Arrays.asList("sci-fi", "action", "thriller"));

        // When
        Model savedMovie = movieRepository.save(movie);

        // Then
        assertThat(savedMovie.getId()).isNotNull();
        assertThat(savedMovie.getTitle()).isEqualTo("The Matrix");
        assertThat(savedMovie.getDescription()).isEqualTo("A computer hacker learns the truth about reality");
        assertThat(savedMovie.getTags()).containsExactly("sci-fi", "action", "thriller");
        assertThat(savedMovie.isDone()).isFalse(); // Default value

        // Verify it can be found
        Optional<Model> foundMovie = movieRepository.findById(savedMovie.getId());
        assertThat(foundMovie).isPresent();
        assertThat(foundMovie.get().getTitle()).isEqualTo("The Matrix");
    }

    @Test
    void shouldSaveMovieWithMinimalData() {
        // Given
        Model movie = new Model("Simple Movie");

        // When
        Model savedMovie = movieRepository.save(movie);

        // Then
        assertThat(savedMovie.getId()).isNotNull();
        assertThat(savedMovie.getTitle()).isEqualTo("Simple Movie");
        assertThat(savedMovie.getDescription()).isNull();
        assertThat(savedMovie.getTags()).isEmpty(); // Default empty list
        assertThat(savedMovie.isDone()).isFalse(); // Default value
    }

    @Test
    void shouldHandleEmptyRepository() {
        // When
        List<Model> allMovies = movieRepository.findAll();

        // Then
        assertThat(allMovies).isEmpty();
        assertThat(movieRepository.count()).isEqualTo(0);
    }

    @Test
    void shouldDeleteMovie() {
        // Given
        Model movie = new Model("To be deleted");
        Model savedMovie = movieRepository.save(movie);

        // When
        movieRepository.deleteById(savedMovie.getId());

        // Then
        assertThat(movieRepository.findById(savedMovie.getId())).isEmpty();
        assertThat(movieRepository.count()).isEqualTo(0);
    }
}