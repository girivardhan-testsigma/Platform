package com.testsigma.platform.service;

import com.testsigma.platform.model.BrowserStackPlatform;
import com.testsigma.platform.model.PlatformBrowser;
import com.testsigma.platform.repository.BrowserStackPlatformRepository;
import com.testsigma.platform.repository.PlatformBrowserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BrowserStackService {

  private final WebClient webClient;
  private final BrowserStackPlatformRepository browserStackPlatformRepository;
  private final PlatformBrowserRepository platformBrowserRepository;

  public BrowserStackService(WebClient.Builder webClientBuilder,
                             BrowserStackPlatformRepository browserStackPlatformRepository,
                             PlatformBrowserRepository platformBrowserRepository) {
    this.webClient = webClientBuilder.baseUrl("https://api.browserstack.com").build();
    this.browserStackPlatformRepository = browserStackPlatformRepository;
    this.platformBrowserRepository = platformBrowserRepository;
  }

  public Mono<Void> fetchAndSaveBrowserStackPlatforms() {
    return webClient.get()
        .uri("/automate/browsers.json")
        .retrieve()
        .bodyToFlux(BrowserStackPlatform.class)
        .flatMap(platform -> {
          String key = generateKey(platform.getOsName(), platform.getBrowserName(), platform.getBrowserVersion());
          BrowserStackPlatform bsBrowser = new BrowserStackPlatform(null, key, platform.getOsName(), platform.getOsVersion(), platform.getBrowserName(), platform.getBrowserVersion());

          return browserStackPlatformRepository.findByPlatformKey(key)
              .switchIfEmpty(browserStackPlatformRepository.save(bsBrowser))
              .then(platformBrowserRepository.findByPlatformKey(key)
                  .switchIfEmpty(Mono.defer(() -> {
                    PlatformBrowser platformBrowser = new PlatformBrowser(null, key, platform.getOsName(), platform.getOsVersion(), platform.getBrowserName(), platform.getBrowserVersion(), true, false, false, false);
                    return platformBrowserRepository.save(platformBrowser);
                  }))
              );
        })
        .then();
  }

  private String generateKey(String osName, String browserName, String browserVersion) {
    return osName + "_" + browserName + "_" + browserVersion;
  }
}