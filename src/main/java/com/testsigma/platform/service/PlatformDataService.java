package com.testsigma.platform.service;

import com.testsigma.platform.dto.BrowserStackPlatformDTO;
import com.testsigma.platform.dto.LambdaTestPlatformDTO;
import com.testsigma.platform.dto.SauceLabsPlatformDTO;
import com.testsigma.platform.model.BrowserStackPlatform;
import com.testsigma.platform.model.LambdaTestPlatform;
import com.testsigma.platform.model.PlatformBrowser;
import com.testsigma.platform.model.SauceLabsPlatform;
import com.testsigma.platform.repository.PlatformBrowserRepository;
import com.testsigma.platform.repository.BrowserStackPlatformRepository;
import com.testsigma.platform.repository.LambdaTestPlatformRepository;
import com.testsigma.platform.repository.SauceLabsPlatformRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlatformDataService {

  private final PlatformBrowserRepository platformBrowserRepository;
  private final BrowserStackPlatformRepository browserStackPlatformRepository;
  private final LambdaTestPlatformRepository lambdaTestPlatformRepository;
  private final SauceLabsPlatformRepository sauceLabsPlatformRepository;

  public PlatformDataService(PlatformBrowserRepository platformBrowserRepository,
                             BrowserStackPlatformRepository browserStackPlatformRepository,
                             LambdaTestPlatformRepository lambdaTestPlatformRepository,
                             SauceLabsPlatformRepository sauceLabsPlatformRepository) {
    this.platformBrowserRepository = platformBrowserRepository;
    this.browserStackPlatformRepository = browserStackPlatformRepository;
    this.lambdaTestPlatformRepository = lambdaTestPlatformRepository;
    this.sauceLabsPlatformRepository = sauceLabsPlatformRepository;
  }

  public Mono<Void> saveBrowserStackData(Flux<BrowserStackPlatformDTO> data) {
    return data.flatMap(dto -> {
      String key = generateKey(dto.getOs(), dto.getOs_version(), dto.getBrowser(), dto.getBrowser_version());
      return platformBrowserRepository.findByPlatformKey(key)
          .switchIfEmpty(createNewPlatformBrowser(dto, key))
          .flatMap(platformBrowser -> {
            platformBrowser.setIsBsSupported(true); // Set BrowserStack support to true
            return platformBrowserRepository.save(platformBrowser)
                .then(browserStackPlatformRepository.save(new BrowserStackPlatform(null,key, dto.getOs(), dto.getOs_version(), dto.getBrowser(), dto.getBrowser_version())));
          });
    }).then();
  }

  public Mono<Void> saveLambdaTestData(Flux<LambdaTestPlatformDTO> data) {
    return data.flatMap(dto -> {
      // Assuming you want to set support for the first browser in the list
      if (dto.getBrowsers() != null && !dto.getBrowsers().isEmpty()) {
        LambdaTestPlatformDTO.BrowserDTO firstBrowser = dto.getBrowsers().get(0);
        String key = generateKey(dto.getPlatform(), firstBrowser.getVersion(), firstBrowser.getBrowser_name(), firstBrowser.getVersion());
        return platformBrowserRepository.findByPlatformKey(key)
            .switchIfEmpty(createNewPlatformBrowser(dto, key))
            .flatMap(platformBrowser -> {
              platformBrowser.setIsLtSupported(true); // Set LambdaTest support to true
              return lambdaTestPlatformRepository.save(new LambdaTestPlatform(null,key,dto.getPlatform(),firstBrowser.getSlug(), firstBrowser.getBrowser_name(), firstBrowser.getVersion()));
            });
      }
      return Mono.empty();
    }).then();
  }

  public Mono<Void> saveSauceLabsData(Flux<SauceLabsPlatformDTO> data) {
    return data.flatMap(dto -> {
      String key = generateKey(dto.getOs(), dto.getLong_version(), dto.getLong_name(), dto.getLatest_stable_version());
      return platformBrowserRepository.findByPlatformKey(key)
          .switchIfEmpty(createNewPlatformBrowser(dto, key))
          .flatMap(platformBrowser -> {
            platformBrowser.setIsSlSupported(true); // Set Sauce Labs support to true
            return platformBrowserRepository.save(platformBrowser)
                .then(sauceLabsPlatformRepository.save(new SauceLabsPlatform(null,key, dto.getOs(), dto.getShort_version(), dto.getLong_name(), dto.getLong_version())));
          });
    }).then();
  }

  private String generateKey(String osName, String osVersion, String browserName, String browserVersion) {
    return osName + "-" + osVersion + "-" + browserName + "-" + browserVersion;
  }

  private Mono<PlatformBrowser> createNewPlatformBrowser(BrowserStackPlatformDTO dto, String key) {
    PlatformBrowser platformBrowser = new PlatformBrowser();
    platformBrowser.setPlatformKey(key);
    platformBrowser.setOsName(dto.getOs());
    platformBrowser.setOsVersion(dto.getOs_version());
    platformBrowser.setBrowserName(dto.getBrowser());
    platformBrowser.setBrowserVersion(dto.getBrowser_version());
    return platformBrowserRepository.save(platformBrowser);
  }

  // Overloaded method for creating a new PlatformBrowser from LambdaTest DTO
  private Mono<PlatformBrowser> createNewPlatformBrowser(LambdaTestPlatformDTO dto, String key) {
    PlatformBrowser platformBrowser = new PlatformBrowser();
    platformBrowser.setPlatformKey(key);
    platformBrowser.setOsName(dto.getPlatform());
    // Assuming you want to use the first browser details for key creation
    if (dto.getBrowsers() != null && !dto.getBrowsers().isEmpty()) {
      LambdaTestPlatformDTO.BrowserDTO firstBrowser = dto.getBrowsers().get(0);
      platformBrowser.setBrowserName(firstBrowser.getBrowser_name());
      platformBrowser.setBrowserVersion(firstBrowser.getVersion());
    }
    return platformBrowserRepository.save(platformBrowser);
  }

  // Overloaded method for creating a new PlatformBrowser from SauceLabs DTO
  private Mono<PlatformBrowser> createNewPlatformBrowser(SauceLabsPlatformDTO dto, String key) {
    PlatformBrowser platformBrowser = new PlatformBrowser();
    platformBrowser.setPlatformKey(key);
    platformBrowser.setOsName(dto.getOs());
    platformBrowser.setBrowserName(dto.getLong_name());
    platformBrowser.setBrowserVersion(dto.getLong_version());
    return platformBrowserRepository.save(platformBrowser);
  }
}
