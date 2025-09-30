package com.example.movie.wish.list.e2e;

import com.example.movie.wish.list.TestcontainersConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Alert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class OpenAppE2ETest {

    private WebDriver driver;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // works in CI
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);

        waitForAppReady();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void openHomePage() {
        String url = "http://localhost:" + port + "/";
        driver.get(url);

        // Dismiss alert if present
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert detected: " + alert.getText());
            alert.dismiss();
        } catch (org.openqa.selenium.NoAlertPresentException ignored) {
            // No alert present, this is expected in normal runs
        }

        String title = driver.getTitle();
        System.out.println("Page title: " + title);

        assertThat(title).isNotBlank();
    }

    private void waitForAppReady() {
        int maxRetries = 30;
        int interval = 1000;
        boolean isUp = false;

        String url = "http://localhost:" + port + "/actuator/health";

        for (int i = 0; i < maxRetries; i++) {
            try {
                HttpURLConnection connection = (HttpURLConnection) java.net.URI.create(url).toURL().openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(500);
                connection.connect();
                if (connection.getResponseCode() < 500) {
                    isUp = true;
                    break;
                }
            } catch (IOException ignored) {
                // Ignored because temporary network errors are expected during retries
            }

            try {
                Thread.sleep(interval);
            } catch (InterruptedException ignored) {
                // Ignored because we just want to retry after interval
            }
        }

        if (!isUp) {
            throw new RuntimeException("Backend not responding after " +
                    (maxRetries * interval / 1000) + " seconds at " + url);
        }
    }
}
