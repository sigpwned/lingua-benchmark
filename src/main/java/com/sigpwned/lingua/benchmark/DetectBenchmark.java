/*-
 * =================================LICENSE_START==================================
 * emoji4j-benchmarks
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.lingua.benchmark;

import static java.util.stream.Collectors.toList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.google.common.io.Resources;


@Fork(value = 3) /* jvmArgsAppend = "-XX:+PrintCompilation" */
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@State(Scope.Benchmark)
public class DetectBenchmark {
  public LanguageDetector detector;

  /**
   * Contains exactly 1MB of "random" data sampled from Twitter streaming API. Visually confirmed to
   * be multi-language.
   */
  public List<String> lines;


  @Setup
  public void setupDetectBenchmark() throws IOException {
    detector = LanguageDetectorBuilder.fromAllLanguages().withPreloadedLanguageModels().build();
    try (BufferedReader in = new BufferedReader(new InputStreamReader(
        new GZIPInputStream(Resources.getResource("tweets.txt.gz").openStream()),
        StandardCharsets.UTF_8))) {
      lines = in.lines().collect(toList());
    }

  }

  /*
   * @formatter:off
   * 
   * As of 2022-03-27:
   * 
   * Benchmark                         Mode  Cnt    Score   Error  Units
   * EmojiJavaBenchmark.tweets        thrpt   15  105.708 ± 0.401  ops/s
   * 
   * @formatter:on
   */
  @Benchmark
  public void detect(Blackhole blackhole) {
    Set<Language> languages = EnumSet.noneOf(Language.class);
    for (String line : lines)
      languages.add(detector.detectLanguageOf(line));
    blackhole.consume(languages);
  }
}
