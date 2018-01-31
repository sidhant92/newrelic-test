name := "newrelic-test"

version := "1.0"

scalaVersion := "2.11.9"

fork in run := true

// Add Depedencies

libraryDependencies += "com.typesafe.akka" %% "akka-actor"        % "2.4.0"
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j"        % "2.4.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http"         % "10.0.11"
libraryDependencies += "com.typesafe.akka" %% "akka-http-caching" % "10.0.11"


javaOptions += "-javaagent:lib/newrelic.jar"
javaOptions += "-Dnewrelic.config.file=src/main/resources/newrelic.yml"
