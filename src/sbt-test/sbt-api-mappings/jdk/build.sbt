val check = TaskKey[Unit]("check")
val checkDoc = TaskKey[Unit]("checkDoc")

def regexMatches(matcher: scala.util.matching.Regex)(str: String): Boolean = {
  matcher.findFirstIn(str).isDefined
}

Global / scalaVersion := "2.12.20"

crossScalaVersions := Seq("2.12.20", "2.13.16")

check := {

  // Avoid `sLog.value`: on sbt 2.x a Logger has no HashWriter, so reading it as a
  // task input breaks the default task caching. `println` needs no task input.
  val compileApiMappings = (Compile / doc / apiMappings).value

  val expect = "https://docs.oracle.com/(?:en/java/)?javase/\\d+/docs/api/".r

  val found = compileApiMappings.values.exists { url =>
    regexMatches(expect)(url.toString)
  }

  if (found) {
    println("Found javadoc url in apiMappings")

  } else {
    println("Entries in apiMappings:")
    compileApiMappings.values
      .map("+ " + _.toString)
      .foreach(
        println(_)
      )
    sys.error(s"Failed to match ${expect}")
  }
}

// Verify the generated scaladoc, locating the output directory via
// `Compile / doc / target` rather than a hard-coded path: on sbt 1.x that is
// `target/api`, but on sbt 2.x it is `target/out/jvm/<...>/api`.
checkDoc := {
  val docDir = (Compile / doc / target).value

  def requireFile(name: String): File = {
    val f = docDir / name
    if (!f.exists) sys.error(s"Expected generated doc file does not exist: $f")
    f
  }

  requireFile("index.html")
  val aHtml = requireFile("A.html")

  val expect =
    "https://docs.oracle.com/(?:en/java/)?javase/\\d+/docs/api/(?:java.base/)?java/lang/Throwable.html".r
  val content = IO.readLines(aHtml)
  if (content.exists(regexMatches(expect))) {
    println(s"Found JDK javadoc link in $aHtml")
  } else {
    sys.error(s"Failed to find a JDK javadoc link in $aHtml")
  }
}
