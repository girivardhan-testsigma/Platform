//package com.testsigma.platform.service;
//
//import com.testsigma.platform.model.LambdaTestPlatform;
//import com.testsigma.platform.model.PlatformBrowser;
//import com.testsigma.platform.repository.LambdaTestPlatformRepository;
//import com.testsigma.platform.repository.PlatformBrowserRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Service
//public class LambdaTestService {
//
//  private final WebClient webClient;
//  private final LambdaTestPlatformRepository lambdaTestPlatformRepository;
//  private final PlatformBrowserRepository platformBrowserRepository;
//
//  public LambdaTestService(WebClient.Builder webClientBuilder,
//                           LambdaTestPlatformRepository lambdaTestPlatformRepository,
//                           PlatformBrowserRepository platformBrowserRepository) {
//    this.webClient = webClientBuilder.baseUrl("https://api.lambdatest.com").build();
//    this.lambdaTestPlatformRepository = lambdaTestPlatformRepository;
//    this.platformBrowserRepository = platformBrowserRepository;
//  }
//
//  public Mono<Void> fetchAndSaveLambdaTestPlatforms() {
//    return webClient.get()
//        .uri("/automation/api/v1/platforms")
//        .retrieve()
//        .bodyToMono(LambdaTestPlatform.class)
//        .flatMapMany(response -> Flux.fromIterable(response.getPlatforms().getDesktop()))
//        .flatMap(platform -> Flux.fromIterable(platform.getBrowsers())
//            .flatMap(browser -> {
//              String key = generateKey(platform.getPlatform(), browser.getBrowserName(), browser.getVersion());
//              LambdaTestPlatform ltBrowser = new LambdaTestPlatform(null, key, platform.getPlatform(), null, browser.getBrowserName(), browser.getVersion(), String.join(",", platform.getResolutions()));
//
//              return lambdaTestPlatformRepository.findByKey(key)
//                  .switchIfEmpty(lambdaTestPlatformRepository.save(ltBrowser))
//                  .then(platformBrowserRepository.findByKey(key)
//                      .switchIfEmpty(Mono.defer(() -> {
//                        PlatformBrowser platformBrowser = new PlatformBrowser(null, key, platform.getPlatform(), null, browser.getBrowserName(), browser.getVersion(), false, true, false, false);
//                        return platformBrowserRepository.save(platformBrowser);
//                      }))
//                  );
//            }))
//        .then();
//  }
//
//  private String generateKey(String osName, String browserName, String browserVersion) {
//    return osName + "_" + browserName + "_" + browserVersion;
//  }
//}