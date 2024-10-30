package com.testsigma.platform.repository;

import com.testsigma.platform.model.SauceLabsPlatform;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SauceLabsPlatformRepository extends ReactiveCrudRepository<SauceLabsPlatform, Long> {
  Mono<SauceLabsPlatform> findByPlatformKey(String key);
}