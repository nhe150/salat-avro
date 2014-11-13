name := "salat-avro"

version := "0.10.0-SNAPSHOT"

organization := "com.banno.salat.avro"

scalaVersion := "2.10.4"

publishMavenStyle := true

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.4" % "test",
  "com.novus" %% "salat" % "2.0.0-SNAPSHOT",
  "org.json4s" %% "json4s-native" % "3.2.5",
  "com.github.nscala-time" %% "nscala-time" % "0.6.0",
  "org.apache.avro" % "avro" % "1.7.5"
)
