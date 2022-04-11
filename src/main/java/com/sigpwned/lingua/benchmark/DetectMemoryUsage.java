package com.sigpwned.lingua.benchmark;

import org.openjdk.jol.info.GraphLayout;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;

public class DetectMemoryUsage {
  public static void main(String[] args) throws Exception {
    LanguageDetector detector =
        LanguageDetectorBuilder.fromAllLanguages().withPreloadedLanguageModels().build();
    System.out.println(GraphLayout.parseInstance(detector).toFootprint());
  }
}
