import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._
import org.scalastyle.sbt.ScalastylePlugin

object BuildSettings {
  lazy val repos = Seq("releases").map(Resolver.sonatypeRepo) ++ Seq("Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases")

  lazy val buildSettings = Seq (
    organization := "com.garymalouf",
    scalaVersion := "2.11.4",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
    testOptions in Test := Seq(Tests.Filter(s => s.endsWith("Spec"))),
    resolvers ++= repos
  )

  lazy val standardSettings = buildSettings ++ ScalastylePlugin.Settings
 
  lazy val customAssemblySettings = Seq(artifact in (Compile, assembly) ~= { art =>
      art.copy(`classifier` = Some("assembly"))
    }, mainClass in assembly := Some("com.garymalouf.HelloWorld")) ++ addArtifact(artifact in (Compile, assembly), assembly)

}

object Dependencies {
  val slf4jVersion = "1.7.7"
  val slf4j = "org.slf4j" % "slf4j-api" % slf4jVersion
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"
  val nscalaTime = "com.github.nscala-time" %% "nscala-time" % "1.6.0"
  val scalaz = "org.scalaz" %% "scalaz-core" % "7.1.0"
  val specs2 = "org.specs2" %% "specs2" % "2.4.14" % "test"
  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.12.1" % "test"
  /*
  val sprayVersion = "1.3.2"
  val akkaVersion = "2.3.7"
  val sprayCan = "io.spray" %% "spray-can" % sprayVersion
  val sprayRouting = "io.spray" %% "spray-routing" % sprayVersion
  val sprayClient = "io.spray" %% "spray-client" % sprayVersion
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  */
}

object HelloWorldBuild extends Build {
  import BuildSettings._
  import Dependencies._
  
  val rootDeps = Seq(slf4j, scalaLogging, logback, nscalaTime, scalaz, specs2, scalaCheck/*, sprayCan, sprayRouting, akkaActor*/)

  val root = Project(
    id = "sbt-hello-world",
    base = file("."),
    settings = standardSettings ++ customAssemblySettings
      ++ Seq(libraryDependencies ++= rootDeps)
  ) 
}
