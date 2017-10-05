organization in ThisBuild := "com.shipoo"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

lazy val `shipoo` = (project in file("."))
  .aggregate(`shipoo-api`, `shipoo-impl`, `shipoo-stream-api`, `shipoo-stream-impl`, `shipoo-ui`)

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
      lagomJavadslClient
    ),
    PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value
  )


val lombok = "org.projectlombok" % "lombok" % "1.16.10"

def common = Seq(
  javacOptions in compile += "-parameters"
)

