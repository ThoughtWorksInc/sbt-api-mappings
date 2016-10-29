libraryDependencies <+= (sbtBinaryVersion in update, scalaBinaryVersion in update, version in ThisBuild) {
  (sbtV, scalaV, pluginVersion) =>
    Defaults.sbtPluginExtra("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % pluginVersion, sbtV, scalaV)
}

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.4")

resolvers += "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven/"

addSbtPlugin("org.spark-packages" % "sbt-spark-package" % "0.2.5")
