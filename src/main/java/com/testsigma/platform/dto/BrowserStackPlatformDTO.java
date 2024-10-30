package com.testsigma.platform.dto;

import lombok.Data;

@Data
public class BrowserStackPlatformDTO {
  private String os;                           // OS Name
  private String os_version;                   // OS Version
  private String browser;                      // Browser Name
  private String browser_version;              // Browser Version
  private String device;                       // Device Name
  private String real_mobile;                  // Real Mobile Device
}
