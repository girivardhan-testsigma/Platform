//package com.testsigma.platform.service;
//
//import com.testsigma.platform.model.PlatformBrowser;
//import com.testsigma.platform.model.SauceLabsPlatform;
//import com.testsigma.platform.repository.SauceLabsPlatformRepository;
//import com.testsigma.platform.repository.PlatformBrowserRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//@Service
//public class SauceLabsService {
//
//  private final WebClient webClient;
//  private final SauceLabsPlatformRepository sauceLabsPlatformRepository;
//  private final PlatformBrowserRepository platformBrowserRepository;
//
//  public SauceLabsService(WebClient.Builder webClientBuilder,
//                          SauceLabsPlatformRepository sauceLabsPlatformRepository,
//                          PlatformBrowserRepository platformBrowserRepository) {
//    this.webClient = webClientBuilder.baseUrl("https://api.us-west-1.saucelabs.com").build();
//    this.sauceLabsPlatformRepository = sauceLabsPlatformRepository;
//    this.platformBrowserRepository = platformBrowserRepository;
//  }
//
//  public Mono<Void> fetchAndSaveSauceLabsPlatforms() {
//    return webClient.get()
//        .uri("/rest/v1/info/platforms/webdriver")
//        .retrieve()
//        .bodyToFlux(SauceLabsPlatform.class)
//        .flatMap(platform -> {
//          String key = generateKey(platform.getOs(), platform.getLongName(), platform.getLongVersion());
//          SauceLabsPlatform slBrowser = new SauceLabsPlatform(null, key, platform.getOs(), null, platform.getLongName(), platform.getLongVersion(), null);
//
//          return sauceLabsPlatformRepository.findByKey(key)
//              .switchIfEmpty(sauceLabsPlatformRepository.save(slBrowser))
//              .then(platformBrowserRepository.findByKey(key)
//                  .switchIfEmpty(Mono.defer(() -> {
//                    PlatformBrowser platformBrowser = new PlatformBrowser(null, key, platform.getOs(), null, platform.getLongName(), platform.getLongVersion(), false, false, true, false);
//                    return platformBrowserRepository.save(platformBrowser);
//                  }))
//              );
//        })
//        .then();
//  }
//
//  private String generateKey(String osName, String browserName, String browserVersion) {
//    return osName + "_" + browserName + "_" + browserVersion;
//  }
//}