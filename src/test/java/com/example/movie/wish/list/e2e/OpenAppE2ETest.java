package com.example.movie.wish.list.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Alert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

class OpenAppE2ETest {

    private WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    static void waitForBackend() {
        System.out.println("🔍 Waiting for backend at " + BASE_URL);

        await()
                .atMost(Duration.ofSeconds(60))
                .pollInterval(Duration.ofSeconds(2))
                .until(() -> {
                    try {
                        HttpURLConnection connection = (HttpURLConnection)
                                java.net.URI.create(BASE_URL + "/actuator/health").toURL().openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(2000);
                        connection.setReadTimeout(2000);
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        connection.disconnect();
                        return responseCode == 200;
                    } catch (IOException e) {
                        return false;
                    }
                });

        System.out.println("✅ Backend is ready!");
    }

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void openHomePage() {
        driver.get(BASE_URL + "/");

        // Wait a bit for JavaScript to load
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(500))
                .ignoreExceptions()
                .until(() -> {
                    try {
                        // Try to get title - if alert appears, handle it
                        return driver.getTitle() != null;
                    } catch (org.openqa.selenium.UnhandledAlertException e) {
                        Alert alert = driver.switchTo().alert();
                        System.out.println("⚠️ Alert detected: " + alert.getText());
                        alert.dismiss();
                        return false; // Retry
                    }
                });

        String title = driver.getTitle();
        System.out.println("✅ Page title: " + title);

        assertThat(title).isNotBlank();
    }
}
