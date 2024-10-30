package com.testsigma.platform.dto;

import lombok.Data;
import java.util.List;

@Data
public class LambdaTestPlatformDTO {
  private String platform;                     // OS Name
  private List<BrowserDTO> browsers;           // List of browsers available on this platform

  @Data
  public static class BrowserDTO {
    private String browser_name;             // Browser Name
    private String version;                  // Browser Version
    private String type;                     // Browser Type (e.g., stable)
    private String slug;                     // Slug identifier for version
  }

  private List<String> resolutions;            // Available screen resolutions for the platform
}
