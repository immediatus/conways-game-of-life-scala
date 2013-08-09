import sbt._
import Keys._

object Build extends sbt.Build {
  import Dependencies._

  lazy val myProject = Project("scala-conway", file("."))
    .settings(
      organization  := "com.epam",
      version       := "0.1",
      scalaVersion  := "2.10.1",
      scalacOptions := Seq("-deprecation", "-unchecked", "-encoding", "utf8"),
      resolvers     ++= Dependencies.resolutionRepos,
      libraryDependencies ++=
        compile(swing, scalaz) ++
        test(specs2)
    )
}
