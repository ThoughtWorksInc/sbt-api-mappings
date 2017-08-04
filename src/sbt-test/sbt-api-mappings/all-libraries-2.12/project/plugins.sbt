libraryDependencies += Defaults.sbtPluginExtra("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % (version in ThisBuild).value, (sbtBinaryVersion in update).value, (scalaBinaryVersion in update).value)

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.1")
