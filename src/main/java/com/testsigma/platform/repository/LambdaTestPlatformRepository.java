package com.testsigma.platform.repository;

import com.testsigma.platform.model.LambdaTestPlatform;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LambdaTestPlatformRepository extends ReactiveCrudRepository<LambdaTestPlatform, Long> {
  Mono<LambdaTestPlatform> findByPlatformKey(String key);
}