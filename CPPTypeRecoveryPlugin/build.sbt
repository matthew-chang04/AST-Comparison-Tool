ThisBuild / scalaVersion := "2.13.12"

val joernVersion = "4.0.937"
val cpgVersion = "1.6.0" 

name := "joern-cpp-type-recovery"
version := "1.0.0"

libraryDependencies ++= Seq(
  "io.joern" %% "joern-cli" % joernVersion,
  "io.joern" %% "semanticcpg" % joernVersion,
  "io.joern" %% "x2cpg" % joernVersion,
  "io.shiftleft" %% "codepropertygraph" % cpgVersion,
  "io.shiftleft" %% "codepropertygraph-domain" % cpgVersion,
  "io.joern" %% "c2cpg" % joernVersion,
  "org.scalatest" %% "scalatest" % "3.2.17" % Test,
  "io.joern" %% "semanticcpg-tests" % joernVersion % Test
)

lazy val createDistribution = taskKey[Unit]("Create plugin distribution")
createDistribution := {
  val log = streams.value.log
  val distDir = target.value / "distributions"
  val zipFile = distDir / s"${name.value}-${version.value}.zip"
  
  IO.createDirectory(distDir)
  val jarFile = (Compile / packageBin).value
  
  log.info(s"Creating distribution: $zipFile")
  IO.zip(Seq(jarFile -> s"${name.value}-${version.value}.jar"), zipFile)
  log.info(s"Distribution created: $zipFile")
}

