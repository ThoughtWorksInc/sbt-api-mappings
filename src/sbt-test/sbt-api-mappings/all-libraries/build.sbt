import java.net.HttpURLConnection

enablePlugins(PlayScala)

sparkVersion := "2.0.1"

sparkComponents += "sql"

def isDownloadableApiDocumentation(url: URL) = {
  val connection = url.openConnection().asInstanceOf[HttpURLConnection]
  try {
    connection.getResponseCode == 200
  } finally {
    connection.disconnect()
  }
}

TaskKey[Unit]("check-scala") := {
  assert(isDownloadableApiDocumentation(apiMappings.value(scalaInstance.value.libraryJar)))
}

crossScalaVersions := Seq("2.10.6", "2.11.8")
