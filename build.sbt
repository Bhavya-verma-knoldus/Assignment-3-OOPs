name := "Assignment 3 (OOPs)"

version := "0.1"

scalaVersion := "2.12.12"

lazy val userManagementSystem = project.in(file("UserManagementSystem"))
  .settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.3" % Test,
      "org.mockito" %% "mockito-scala" % "1.5.12" % Test
    )
  )

lazy val root = project.in(file(".")).aggregate(userManagementSystem)