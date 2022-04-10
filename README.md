# lingua-benchmark

To build, use:

    mvn clean compile install

To run, use:

    java -jar target/benchmarks.jar -prof gc

You will want to have multiple versions of lingua available on the
local machine, probably in the local maven repository cache, and
switch between lingua versions in the POM to see how different
approaches perform in this benchmark.