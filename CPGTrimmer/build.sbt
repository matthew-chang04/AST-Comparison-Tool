ThisBuild / scalaVersion := "3.3.1"
ThisBuild / organization := "io.joern"

lazy val root = (project in file("."))
  .settings(
    name := "cpg-trimmer-plugin",
    version := "1.0.0",
    
    libraryDependencies ++= Seq(
      // Core Joern dependencies
      "io.joern" %% "semanticcpg" % "2.0.0",
      "io.joern" %% "codepropertygraph" % "2.0.0",
      "io.joern" %% "console" % "2.0.0",
      
      // For better-files
      "com.github.pathikrit" %% "better-files" % "3.9.2",
      
      // For JGit (Git operations)
      "org.eclipse.jgit" % "org.eclipse.jgit" % "6.7.0.202309050840-r",
      
      // Scala collection converters
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.11.0",
      
      // Testing dependencies
      "org.scalatest" %% "scalatest" % "3.2.17" % Test,
      "io.joern" %% "c2cpg" % "2.0.0" % Test // For creating test CPGs
    ),
    
    // Export JAR with dependencies
    exportJars := true,
    
    // Compiler options
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature", 
      "-unchecked",
      "-Wunused:imports"
    ),
    
    // JVM options for better performance
    javaOptions ++= Seq(
      "-Xmx4G",
      "-XX:+UseG1GC"
    ),
    
    // Resolver for Joern artifacts
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
    )
  )
