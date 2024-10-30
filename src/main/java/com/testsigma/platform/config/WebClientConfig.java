package com.testsigma.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Value("${lambdatest.api.url}")
  private String lambdaTestUrl;
  @Value("${saucelabs.api.url}")
  private String sauceLabsUrl;
  @Value("${browserstack.api.url}")
  private String browserStackUrl;

  @Value("${lambdatest.api.username}")
  private String lambdaTestUsername;
  @Value("${lambdatest.api.password}")
  private String lambdaTestPassword;
  @Value("${saucelabs.api.username}")
  private String sauceLabsUsername;
  @Value("${saucelabs.api.password}")
  private String sauceLabsPassword;
  @Value("${browserstack.api.username}")
  private String browserStackUsername;
  @Value("${browserstack.api.password}")
  private String browserStackPassword;

  @Bean
  public WebClient lambdaTestClient() {
    return WebClient.builder()
        .baseUrl(lambdaTestUrl)
        .defaultHeaders(headers -> headers.setBasicAuth(lambdaTestUsername, lambdaTestPassword))
        .build();
  }

  @Bean
  public WebClient sauceLabsClient() {
    return WebClient.builder()
        .baseUrl(sauceLabsUrl)
        .defaultHeaders(headers -> headers.setBasicAuth(sauceLabsUsername, sauceLabsPassword))
        .build();
  }

  @Bean
  public WebClient browserStackClient() {
    return WebClient.builder()
        .baseUrl(browserStackUrl)
        .defaultHeaders(headers -> headers.setBasicAuth(browserStackUsername, browserStackPassword))
        .build();
  }
}
