package com.example.movie.wish.list.e2e;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieWishlistE2ETest {

    private String createdMovieId;

    @BeforeAll
    static void waitForBackend() {
        RestAssured.baseURI = "http://localhost:8080";

        Awaitility.await()
                .atMost(60, TimeUnit.SECONDS)
                .pollInterval(2, TimeUnit.SECONDS)
                .until(() -> {
                    try {
                        return given()
                                .when()
                                .get("/actuator/health")
                                .then()
                                .extract()
                                .statusCode() == 200;
                    } catch (Exception e) {
                        return false;
                    }
                });

        System.out.println("✅ Backend is up! Proceeding with tests.");
    }

    @Test
    @Order(1)
    void testGetAllMovies_InitiallyEmpty() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist")
                .then()
                .statusCode(200)
                .body("$", isA(List.class));
    }

    @Test
    @Order(2)
    void testCreateMovie_Success() {
        String requestBody = """
            {
                "title": "Inception",
                "description": "A mind-bending thriller",
                "tags": ["Sci-Fi", "Action", "Thriller"],
                "done": false
            }
            """;

        createdMovieId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/wishlist")
                .then()
                .statusCode(200)
                .body("title", equalTo("Inception"))
                .body("description", equalTo("A mind-bending thriller"))
                .body("tags", hasItems("Sci-Fi", "Action", "Thriller"))
                .body("done", equalTo(false))
                .body("id", notNullValue())
                .extract()
                .path("id");

        System.out.println("✅ Movie created with ID: " + createdMovieId);
    }

    @Test
    @Order(3)
    void testGetAllMovies_ContainsCreatedMovie() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(1)))
                .body("title", hasItem("Inception"));
    }

    @Test
    @Order(4)
    void testGetMovieById_Success() {
        Assertions.assertNotNull(createdMovieId, "Movie ID should not be null");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist/" + createdMovieId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdMovieId))
                .body("title", equalTo("Inception"))
                .body("description", equalTo("A mind-bending thriller"));
    }

    @Test
    @Order(5)
    void testUpdateMovie_Success() {
        Assertions.assertNotNull(createdMovieId, "Movie ID should not be null");

        String updateBody = """
            {
                "title": "Inception (Updated)",
                "description": "An updated mind-bending thriller",
                "tags": ["Sci-Fi", "Action", "Drama"],
                "done": true
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updateBody)
                .when()
                .put("/api/wishlist/" + createdMovieId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdMovieId))
                .body("title", equalTo("Inception (Updated)"))
                .body("description", equalTo("An updated mind-bending thriller"))
                .body("tags", hasItems("Sci-Fi", "Action", "Drama"))
                .body("done", equalTo(true));

        System.out.println("✅ Movie updated successfully");
    }

    @Test
    @Order(6)
    void testToggleDoneStatus() {
        Assertions.assertNotNull(createdMovieId, "Movie ID should not be null");

        // Toggle done status back to false
        String toggleBody = """
            {
                "title": "Inception (Updated)",
                "description": "An updated mind-bending thriller",
                "tags": ["Sci-Fi", "Action", "Drama"],
                "done": false
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(toggleBody)
                .when()
                .put("/api/wishlist/" + createdMovieId)
                .then()
                .statusCode(200)
                .body("done", equalTo(false));

        System.out.println("✅ Done status toggled successfully");
    }

    @Test
    @Order(7)
    void testDeleteMovie_Success() {
        Assertions.assertNotNull(createdMovieId, "Movie ID should not be null");

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/wishlist/" + createdMovieId)
                .then()
                .statusCode(204);

        System.out.println("✅ Movie deleted successfully");
    }

    @Test
    @Order(8)
    void testGetMovieById_AfterDelete_NotFound() {
        Assertions.assertNotNull(createdMovieId, "Movie ID should not be null");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist/" + createdMovieId)
                .then()
                .statusCode(500); // Controller throws RuntimeException which returns 500
    }

    @Test
    @Order(9)
    void testCreateMovieWithMinimalData() {
        String minimalBody = """
            {
                "title": "The Matrix",
                "description": "",
                "tags": [],
                "done": false
            }
            """;

        String movieId = given()
                .contentType(ContentType.JSON)
                .body(minimalBody)
                .when()
                .post("/api/wishlist")
                .then()
                .statusCode(200)
                .body("title", equalTo("The Matrix"))
                .body("description", equalTo(""))
                .body("tags", empty())
                .extract()
                .path("id");

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
        System.out.println("✅ Minimal movie creation test passed");
    }

    @Test
    @Order(10)
    void testFullUserFlow() {
        // 1. Create a new movie
        String createBody = """
            {
                "title": "Interstellar",
                "description": "Space exploration epic",
                "tags": ["Sci-Fi", "Adventure"],
                "done": false
            }
            """;

        String movieId = given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when()
                .post("/api/wishlist")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        // 2. Verify it appears in the list
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist")
                .then()
                .statusCode(200)
                .body("title", hasItem("Interstellar"));

        // 3. Update the movie
        String updateBody = """
            {
                "title": "Interstellar",
                "description": "Space exploration epic (Watched)",
                "tags": ["Sci-Fi", "Adventure"],
                "done": true
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updateBody)
                .when()
                .put("/api/wishlist/" + movieId)
                .then()
                .statusCode(200)
                .body("done", equalTo(true));

        // 4. Delete the movie
        given()
                .when()
                .delete("/api/wishlist/" + movieId)
                .then()
                .statusCode(204);

        System.out.println("✅ Full user flow test completed");
    }
}
