organization in ThisBuild := "com.shipoo"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

lazy val `shipoo` = (project in file("."))
  .aggregate(
    `shipoo-test-utils`,
    `shipoo-security`,
    `shipoo-api`,
    `shipoo-impl`,
    `shipoo-user-api`,
    `shipoo-user-impl`,
    `shipoo-stream-api`,
    `shipoo-stream-impl`,
    `shipoo-ui`)

lazy val `shipoo-test-utils` = (project in file("shipoo-test-utils"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomJavadslPersistenceCassandra
    )
  )

lazy val `shipoo-security` = (project in file("shipoo-security"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomJavadslServer % Optional,
      lombok
    )
  )

lazy val `shipoo-user-api` = (project in file("shipoo-user-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `shipoo-user-impl` = (project in file("shipoo-user-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomJavadslTestKit,
      lombok
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`shipoo-test-utils`, `shipoo-security`, `shipoo-user-api`)

lazy val `shipoo-api` = (project in file("shipoo-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `shipoo-impl` = (project in file("shipoo-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomJavadslTestKit,
      lombok
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`shipoo-api`)

lazy val `shipoo-stream-api` = (project in file("shipoo-stream-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi
    )
  )

lazy val `shipoo-stream-impl` = (project in file("shipoo-stream-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaClient,
      lagomJavadslTestKit
    )
  )
  .dependsOn(`shipoo-stream-api`, `shipoo-api`)

lazy val `shipoo-ui` = (project in file("shipoo-ui"))
  .enablePlugins(PlayJava && LagomPlay)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(`shipoo-api`)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslClient,
      lagomJavadslImmutables,
      "org.pac4j" % "play-pac4j" % "3.0.1",
      "org.pac4j" % "pac4j-http" % "2.0.0",
      cache,
      "org.pac4j" % "pac4j-openid" % "2.0.0" exclude("xml-apis" , "xml-apis"),
      "org.pac4j" % "pac4j-oauth" % "2.0.0",
      "org.pac4j" % "pac4j-oidc" % "2.0.0" exclude("commons-io" , "commons-io"),
      "org.pac4j" % "pac4j-jwt" % "2.0.0" exclude("commons-io" , "commons-io")
    ),
    PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value
  )


val lombok = "org.projectlombok" % "lombok" % "1.16.10"

def common = Seq(
  javacOptions in compile += "-parameters"
)

import scala.concurrent.duration._ // Mind that the import is needed.
lagomCassandraMaxBootWaitingTime in ThisBuild := 100.seconds