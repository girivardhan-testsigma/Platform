package com.testsigma.platform.repository;

import com.testsigma.platform.model.BrowserStackPlatform;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BrowserStackPlatformRepository extends ReactiveCrudRepository<BrowserStackPlatform, Long> {
  Mono<BrowserStackPlatform> findByPlatformKey(String key);
}