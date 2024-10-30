package com.testsigma.platform.dto;

import lombok.Data;

@Data
public class SauceLabsPlatformDTO {
  private String os;                           // OS
  private String short_version;                // Short version of the browser
  private String long_name;                    // Long name (browser name)
  private String long_version;                 // Long version of the browser
  private String latest_stable_version;        // Latest stable version
  private String automation_backend;           // Automation backend (webdriver)
}
