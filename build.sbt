name := "Assignment 3 (OOPs)"

version := "0.1"

scalaVersion := "2.12.12"

lazy val userManagementSystem = project.in(file("UserManagementSystem"))

lazy val root = project.in(file(".")).aggregate(userManagementSystem)