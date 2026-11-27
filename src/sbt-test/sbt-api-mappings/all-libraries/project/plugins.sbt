libraryDependencies += Defaults.sbtPluginExtra(
  "com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % sys.props(
    "plugin.version"
  ),
  (update / sbtBinaryVersion).value,
  (update / scalaBinaryVersion).value
)
