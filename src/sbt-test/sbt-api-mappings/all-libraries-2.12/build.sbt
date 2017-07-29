enablePlugins(PlayScala)

def isDownloadableApiDocumentation(url: URL) = {
  import java.net.HttpURLConnection
  val connection = url.openConnection().asInstanceOf[HttpURLConnection]
  try {
    connection.getResponseCode == 200
  } finally {
    connection.disconnect()
  }
}

val check = TaskKey[Unit]("check")

check := {
  assert(isDownloadableApiDocumentation((apiMappings in Compile in doc).value(scalaInstance.value.libraryJar)))
  assert(isDownloadableApiDocumentation((apiMappings in Test in doc).value(scalaInstance.value.libraryJar)))
}

for ((config, exists) <- Seq((Test, true), (Compile, false))) yield {
  check := {
    check.value
    val scalacheckJarName = Artifact.artifactName(ScalaVersion(scalaVersion.value, scalaBinaryVersion.value),
                                                  "org.scalacheck" %% "scalacheck" % "1.13.3",
                                                  Artifact("scalacheck"))
    assert((apiMappings in config in doc).value.exists {
      case (file, url) =>
        file.getName == scalacheckJarName && isDownloadableApiDocumentation(url)
    } == exists)
  }
}

crossScalaVersions := Seq("2.12.3")

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % Test

sources in Test += baseDirectory.value / "test_renamed" / "Test.scala"
