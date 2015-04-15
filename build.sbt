name := """amit-nmap-toy-project"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.21",
  "org.springframework" % "spring-context" % "4.1.1.RELEASE",
  "org.springframework" % "spring-core" % "4.1.1.RELEASE",
  "org.springframework" % "spring-beans" % "4.1.1.RELEASE",
  "org.springframework" % "spring-tx" % "4.1.1.RELEASE",
  "org.springframework" % "spring-orm" % "4.1.1.RELEASE",
  "org.springframework" % "spring-jdbc" % "4.1.1.RELEASE",
  "org.springframework" % "spring-expression" % "4.1.1.RELEASE",
  "org.springframework" % "spring-aop" % "4.1.1.RELEASE",
  "org.springframework" % "spring-test" % "4.1.1.RELEASE" % "test",
  "org.hibernate" % "hibernate-entitymanager" % "4.1.8.Final",
  "junit" % "junit" % "4.8.2",
  "org.powermock" % "powermock-module-junit4" % "1.4.12",
  "org.slf4j" % "slf4j-log4j12" % "1.6.6",
  "org.powermock" % "powermock-api-mockito" % "1.4.12",
  "commons-collections" % "commons-collections" % "3.2",
  "commons-lang" % "commons-lang" % "2.6",
  "com.wordnik" %% "swagger-play2" % "1.3.10",
  "com.google.code.gson" % "gson" % "2.3",
  "org.apache.commons" % "commons-exec" % "1.3",
  "joda-time" % "joda-time" % "2.1",
  "commons-jxpath" % "commons-jxpath" % "1.3",
  "commons-validator" % "commons-validator" % "1.4.0")
