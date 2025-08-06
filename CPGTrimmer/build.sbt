name := "cpg-trimmer"
version := "0.1.0"
scalaVersion := "3.3.1"

val flatgraphVersion = "0.1.9"
val joernVersion = "4.0.402"
val shiftLeftCpgVersion = "1.4.6"

resolvers += "Maven Central" at "https://repo1.maven.org/maven2/"
resolvers += "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/public"

libraryDependencies ++= Seq(
  // Core Joern dependencies
  "io.joern" %% "x2cpg" % joernVersion,
  // Testing dependencies
  "org.scalatest" %% "scalatest" % "3.2.17" % Test
)