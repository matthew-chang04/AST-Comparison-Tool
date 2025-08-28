name := "cpg-trimmer"
ThisBuild/organization := "io.joern"
ThisBuild/scalaVersion := "3.6.4"

// Use mixed versions that actually exist for Scala 3
val joernVersion = "4.0.397"
enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
  ("io.joern" %% "console" % joernVersion)
    .exclude("org.gradle", "gradle-tooling-api"), // Exclude from all deps
  ("io.joern" %% "semanticcpg" % joernVersion)
    .exclude("org.gradle", "gradle-tooling-api"),
  ("io.joern" %% "x2cpg" % joernVersion)
    .exclude("org.gradle", "gradle-tooling-api"),
  ("io.joern" %% "dataflowengineoss" % joernVersion)
    .exclude("org.gradle", "gradle-tooling-api"),

  // Add missing dependency for createDistribution task
  "com.github.pathikrit" %% "better-files" % "3.9.2",

  "org.scalatest" %% "scalatest" % "3.2.17" % Test
)

// Disable documentation generation
Compile/doc/sources := Seq.empty
Compile/packageDoc/publishArtifact := false

lazy val createDistribution = taskKey[Unit]("Create binary distribution of extension")
createDistribution := {
  val pkgBin = (Universal / packageBin).value
  val tmpDstArchive = "/tmp/cpg-trimmer.zip"
  val dstArchive = "cpg-trimmer.zip"

  // Copy the package to temp location
  IO.copy(
    List((pkgBin, file(tmpDstArchive))),
    CopyOptions(overwrite = true, preserveLastModified = true, preserveExecutable = true)
  )

  // Create cleaned distribution
  val f = better.files.File(dstArchive)
  better.files.File.usingTemporaryDirectory("cpg-trimmer") { dir =>
    better.files.File(tmpDstArchive).unzipTo(dir)
    dir.zipTo(f)
    better.files.File(tmpDstArchive).delete()
  }

  println(s"Created distribution: $dstArchive")
}

// Compiler settings
ThisBuild / Compile / scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-feature",
  "-deprecation",
  "-language:implicitConversions",
  "-Wunused:imports",
  "-Wunused:locals",
  "-Wunused:privates"
)

// Java settings - updated for compatibility
ThisBuild / javacOptions ++= Seq("-source", "11", "-target", "11")
ThisBuild / Test / compile / javacOptions ++= Seq("-g", "-target", "11")

// Global / onChangedBuildSource := ReloadOnSourceChanges  // Commented out to fix ClassCastException

fork := true

// Updated resolvers (removed Gradle repo since you don't need it)
ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/public",
)