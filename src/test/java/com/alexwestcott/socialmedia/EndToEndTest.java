package com.alexwestcott.socialmedia;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.chrome.ChromeDriverService.createDefaultService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {
	static ChromeDriverService service;
	static ChromeDriver driver;
	@LocalServerPort
	int port;

	@BeforeAll
	public static void setUp() throws IOException {
		System.setProperty("webdriver.chrome.driver",
				"ext/chromedriver");
		service = createDefaultService();
		driver = new ChromeDriver(service);
		Path testResults = Paths.get("build", "test-results");
		if (!Files.exists(testResults)) {
			Files.createDirectory(testResults);
		}
	}

	@Test
	public void homePageShouldWork(){
		driver.get("localhost:" + port);
		assertThat(driver.getTitle()).isEqualTo("Learning Spring Boot: Spring-a-Gram");

		String pageContent = driver.getPageSource();

		assertThat(pageContent).contains("<a href=\"/images/cat.jpg/raw\">");
		WebElement element = driver.findElement(By.cssSelector("a[href*=\"cat.jpg\"]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().perform();

		assertThat(driver.getCurrentUrl()).isEqualTo(String.format("http://localhost:%s/images/cat.jpg/raw", port));

		driver.navigate().back();
	}

	@AfterAll
	public static void tearDown(){
		service.stop();
	}
}
