package com.example.movie.wish.list.e2e;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class MovieWishlistE2ETest {

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
    void testGetAllMovies_ReturnsListSuccessfully() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist")
                .then()
                .statusCode(200)
                .body("$", isA(List.class));
    }

    @Test
    void testCreateMovie_Success() {
        String requestBody = """
            {
                "title": "Inception",
                "description": "A mind-bending thriller",
                "tags": ["Sci-Fi", "Action", "Thriller"],
                "done": false
            }
            """;

        String movieId = given()
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

        System.out.println("✅ Movie created with ID: " + movieId);

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
    }

    @Test
    void testGetMovieById_Success() {
        // Setup: Create a movie first
        String createBody = """
            {
                "title": "The Dark Knight",
                "description": "A superhero thriller",
                "tags": ["Action", "Crime"],
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

        // Test: Get the movie by ID
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist/" + movieId)
                .then()
                .statusCode(200)
                .body("id", equalTo(movieId))
                .body("title", equalTo("The Dark Knight"))
                .body("description", equalTo("A superhero thriller"));

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
    }

    @Test
    void testUpdateMovie_Success() {
        // Setup: Create a movie first
        String createBody = """
            {
                "title": "Parasite",
                "description": "A South Korean thriller",
                "tags": ["Drama", "Thriller"],
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

        // Test: Update the movie
        String updateBody = """
            {
                "title": "Parasite (Updated)",
                "description": "An award-winning South Korean thriller",
                "tags": ["Drama", "Thriller", "Award Winner"],
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
                .body("id", equalTo(movieId))
                .body("title", equalTo("Parasite (Updated)"))
                .body("description", equalTo("An award-winning South Korean thriller"))
                .body("tags", hasItems("Drama", "Thriller", "Award Winner"))
                .body("done", equalTo(true));

        System.out.println("✅ Movie updated successfully");

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
    }

    @Test
    void testToggleDoneStatus() {
        // Setup: Create a movie with done=true
        String createBody = """
            {
                "title": "Shawshank Redemption",
                "description": "A prison drama",
                "tags": ["Drama"],
                "done": true
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

        // Test: Toggle done status to false
        String toggleBody = """
            {
                "title": "Shawshank Redemption",
                "description": "A prison drama",
                "tags": ["Drama"],
                "done": false
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(toggleBody)
                .when()
                .put("/api/wishlist/" + movieId)
                .then()
                .statusCode(200)
                .body("done", equalTo(false));

        System.out.println("✅ Done status toggled successfully");

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
    }

    @Test
    void testDeleteMovie_Success() {
        // Setup: Create a movie first
        String createBody = """
            {
                "title": "Pulp Fiction",
                "description": "A crime film",
                "tags": ["Crime", "Drama"],
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

        // Test: Delete the movie
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/wishlist/" + movieId)
                .then()
                .statusCode(204);

        System.out.println("✅ Movie deleted successfully");
    }

    @Test
    void testGetMovieById_AfterDelete_NotFound() {
        // Setup: Create and then delete a movie
        String createBody = """
            {
                "title": "Fight Club",
                "description": "A psychological thriller",
                "tags": ["Drama", "Thriller"],
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

        given().delete("/api/wishlist/" + movieId);

        // Test: Try to get deleted movie
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist/" + movieId)
                .then()
                .statusCode(500); // Controller throws RuntimeException which returns 500
    }

    @Test
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

        System.out.println("✅ Minimal movie creation test passed");

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
    }

    @Test
    void testCreateMovieWithLongDescription() {
        String longDescription = "A".repeat(1000); // Test boundary limit
        String requestBody = String.format("""
            {
                "title": "Titanic",
                "description": "%s",
                "tags": ["Drama", "Romance"],
                "done": false
            }
            """, longDescription);

        String movieId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/wishlist")
                .then()
                .statusCode(200)
                .body("title", equalTo("Titanic"))
                .body("description", equalTo(longDescription))
                .extract()
                .path("id");

        System.out.println("✅ Long description test passed");

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
    }

    @Test
    void testCreateMovieWithSpecialCharacters() {
        String requestBody = """
            {
                "title": "Amélie & François: L'été à Paris",
                "description": "A film with special chars: @#$%^&*()_+{}|:<>?",
                "tags": ["French", "Romance", "Comedy"],
                "done": false
            }
            """;

        String movieId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/wishlist")
                .then()
                .statusCode(200)
                .body("title", equalTo("Amélie & François: L'été à Paris"))
                .body("description", containsString("@#$%^&*()_+{}|:<>?"))
                .extract()
                .path("id");

        System.out.println("✅ Special characters test passed");

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
    }

    @Test
    void testCreateMultipleMoviesAndVerifyList() {
        // Create multiple movies
        String movie1Body = """
            {
                "title": "Gladiator",
                "description": "Ancient Rome epic",
                "tags": ["Action", "Drama"],
                "done": false
            }
            """;

        String movie2Body = """
            {
                "title": "The Godfather",
                "description": "Mafia drama",
                "tags": ["Crime", "Drama"],
                "done": true
            }
            """;

        String movieId1 = given()
                .contentType(ContentType.JSON)
                .body(movie1Body)
                .when()
                .post("/api/wishlist")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        String movieId2 = given()
                .contentType(ContentType.JSON)
                .body(movie2Body)
                .when()
                .post("/api/wishlist")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        // Verify both movies appear in the list
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist")
                .then()
                .statusCode(200)
                .body("title", hasItems("Gladiator", "The Godfather"));

        System.out.println("✅ Multiple movies test passed");

        // Cleanup
        given().delete("/api/wishlist/" + movieId1);
        given().delete("/api/wishlist/" + movieId2);
    }

    @Test
    void testUpdateMovieWithEmptyTags() {
        // Setup: Create a movie with tags
        String createBody = """
            {
                "title": "Avatar",
                "description": "Sci-fi adventure",
                "tags": ["Sci-Fi", "Action", "Adventure"],
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

        // Test: Update to remove all tags
        String updateBody = """
            {
                "title": "Avatar",
                "description": "Sci-fi adventure",
                "tags": [],
                "done": false
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updateBody)
                .when()
                .put("/api/wishlist/" + movieId)
                .then()
                .statusCode(200)
                .body("tags", empty());

        System.out.println("✅ Empty tags update test passed");

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
    }

    @Test
    void testGetMovieById_WithInvalidId() {
        // Test: Try to get a movie with non-existent ID
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist/999999")
                .then()
                .statusCode(anyOf(equalTo(404), equalTo(500)));

        System.out.println("✅ Invalid ID test passed");
    }

    @Test
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

        // 3. Retrieve by ID
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist/" + movieId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Interstellar"));

        // 4. Update the movie
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

        // 5. Delete the movie
        given()
                .when()
                .delete("/api/wishlist/" + movieId)
                .then()
                .statusCode(204);

        // 6. Verify deletion
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/wishlist/" + movieId)
                .then()
                .statusCode(anyOf(equalTo(404), equalTo(500)));

        System.out.println("✅ Full user flow test completed");
    }

    @Test
    void testCreateAndUpdateMultipleTimes() {
        // Create a movie
        String createBody = """
            {
                "title": "Forrest Gump",
                "description": "Life story",
                "tags": ["Drama"],
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

        // Update multiple times
        for (int i = 1; i <= 3; i++) {
            String updateBody = String.format("""
                {
                    "title": "Forrest Gump (Version %d)",
                    "description": "Life story - Update %d",
                    "tags": ["Drama", "Update%d"],
                    "done": %b
                }
                """, i, i, i, i % 2 == 0);

            given()
                    .contentType(ContentType.JSON)
                    .body(updateBody)
                    .when()
                    .put("/api/wishlist/" + movieId)
                    .then()
                    .statusCode(200)
                    .body("title", equalTo("Forrest Gump (Version " + i + ")"));
        }

        System.out.println("✅ Multiple updates test passed");

        // Cleanup
        given().delete("/api/wishlist/" + movieId);
    }
}
