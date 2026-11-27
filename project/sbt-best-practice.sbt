val sbtBestPractice =
  "com.thoughtworks.sbt-best-practice" % "sbt-best-practice" % "11.0.0"

// Its published sbt 2 dependency still links against sbt 1-only internals.
addSbtPlugin(
  sbtBestPractice.exclude(
    "com.thoughtworks.sbt-api-mappings",
    "sbt-api-mappings_sbt2_3"
  )
)
