libraryDependencies += Defaults.sbtPluginExtra(
  "com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % sys.props(
    "plugin.version"
  ),
  (sbtBinaryVersion in update).value,
  (scalaBinaryVersion in update).value
)
