name := "cpg-trimmer"
version := "0.1.0"
scalaVersion := "2.13.12"

val flatgraphVersion = "0.1.9"

resolvers += "Maven Central" at "https://repo1.maven.org/maven2/"
resolvers += "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/public"

libraryDependencies ++= Seq(
  // Core Joern dependencies
  "io.shiftleft" %% "semanticcpg" % "1.3.228",
  "io.shiftleft" %% "codepropertygraph" % "1.3.228",
  "io.shiftleft" %% "console" % "1.3.228",
  // Testing dependencies
  "org.scalatest" %% "scalatest" % "3.2.17" % Test
)