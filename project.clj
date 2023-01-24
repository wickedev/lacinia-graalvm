(defproject lacinia "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [com.walmartlabs/lacinia "1.0"]
                 [criterium/criterium "0.4.6"]
                 [org.clojure/core.specs.alpha "0.2.62"]
                 [com.github.clj-easy/graal-build-time "0.1.4"]]

  :main simple.main

  :uberjar-name "simple-main.jar"

  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-shell "0.5.0"]]}}

  :aliases
  {"native"
   ["shell"
    "native-image"
    "--features=InitAtBuildTimeFeature"
    "--report-unsupported-elements-at-runtime"
    "--no-fallback"
    "--initialize-at-build-time=org"
    "--install-exit-handlers"
    "-H:Log=registerResource:"
    "-H:+ReportExceptionStackTraces"
    "-H:+PrintClassInitialization"
    "-H:ConfigurationFileDirectories=./graalvm-config/"
    "-R:MaxHeapSize=32g"
    "-J-Xmx32G"
    "-jar" "./target/${:uberjar-name:-${:name}-${:version}-standalone.jar}"
    "-H:Name=./target/${:name}"]

   "run-native" ["shell" "./target/${:name}"]})
