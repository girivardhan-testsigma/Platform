package com.testsigma.platform;

import com.testsigma.platform.dto.BrowserStackPlatformDTO;
import com.testsigma.platform.dto.LambdaTestPlatformDTO;
import com.testsigma.platform.dto.SauceLabsPlatformDTO;
import com.testsigma.platform.service.PlatformDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class PlatformApplication implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(PlatformApplication.class);
  private final WebClient lambdaTestClient;
  private final WebClient sauceLabsClient;
  private final WebClient browserStackClient;
  @Autowired
  private PlatformDataService platformDataService;

  @Autowired
  public PlatformApplication(WebClient lambdaTestClient, WebClient sauceLabsClient, WebClient browserStackClient) {
    this.lambdaTestClient = lambdaTestClient;
    this.sauceLabsClient = sauceLabsClient;
    this.browserStackClient = browserStackClient;
  }

  public static void main(String[] args) {
    SpringApplication.run(PlatformApplication.class, args);
  }

  @Override
  public void run(String... args) {
    fetchAndStoreLambdaTestData();
    fetchAndStoreSauceLabsData();
    fetchAndStoreBrowserStackData();
  }

  private void fetchAndStoreLambdaTestData() {
    Flux<LambdaTestPlatformDTO> lambdaTestData = lambdaTestClient.get()
        .retrieve()
        .bodyToFlux(LambdaTestPlatformDTO.class)
        .doOnNext(data -> System.out.println("Fetched LambdaTest data: " + data));
    platformDataService.saveLambdaTestData(lambdaTestData)
        .subscribe(
            null,
            error -> logger.error("Error storing LambdaTest data: ", error),
            () -> logger.info("LambdaTest data stored successfully.")
        );

    // Process and store data in the database, similar to the earlier example
  }

  private void fetchAndStoreSauceLabsData() {
    Flux<SauceLabsPlatformDTO> sauceLabsData = sauceLabsClient.get()
        .retrieve()
        .bodyToFlux(SauceLabsPlatformDTO.class)
        .doOnNext(data -> System.out.println("Fetched SauceLabs data: " + data));

    platformDataService.saveSauceLabsData(sauceLabsData)
        .subscribe(
            null,
            error -> logger.error("Error storing SauceLabs data: ", error),
            () -> logger.info("SauceLabs data stored successfully.")
        );

    // Process and store data in the database
  }

  private void fetchAndStoreBrowserStackData() {
    Flux<BrowserStackPlatformDTO> browserStackData = browserStackClient.get()
        .retrieve()
        .bodyToFlux(BrowserStackPlatformDTO.class)
        .filter(platform -> platform.getDevice() == null)
        .doOnNext(data -> System.out.println("Fetched BrowserStack data: " + data));


    platformDataService.saveBrowserStackData(browserStackData)
        .subscribe(
            null,
            error -> logger.error("Error storing BrowserStack data: ", error),
            () -> logger.info("BrowserStack data stored successfully.")
        );
    // Process and store data in the database
  }
}
