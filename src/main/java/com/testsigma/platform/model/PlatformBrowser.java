package com.testsigma.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("platform_browsers")
public class PlatformBrowser {
  @Id
  private Long id;
  private String platformKey;
  private String osName;
  private String osVersion;
  private String browserName;
  private String browserVersion;
  private Boolean isBsSupported = false;
  private Boolean isLtSupported = false;
  private Boolean isSlSupported = false;
  private Boolean helixSupported = false;

  // Additional business logic, if any, can go here
}
