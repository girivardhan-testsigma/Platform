package com.testsigma.platform.repository;


import com.testsigma.platform.model.PlatformBrowser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PlatformBrowserRepository extends ReactiveCrudRepository<PlatformBrowser, Long> {
  Mono<PlatformBrowser> findByPlatformKey(String key);
}