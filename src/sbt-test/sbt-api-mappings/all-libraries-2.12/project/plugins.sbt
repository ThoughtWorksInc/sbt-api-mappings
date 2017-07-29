libraryDependencies <+= (sbtBinaryVersion in update, scalaBinaryVersion in update, version in ThisBuild) {
  (sbtV, scalaV, pluginVersion) =>
    Defaults.sbtPluginExtra("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % pluginVersion, sbtV, scalaV)
}

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.1")
