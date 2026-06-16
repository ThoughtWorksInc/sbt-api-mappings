val check = TaskKey[Unit]("check")

def regexMatches(matcher: scala.util.matching.Regex)(str: String): Boolean = {
  matcher.findFirstIn(str).isDefined
}

Global / scalaVersion := "2.12.10"

crossScalaVersions := Seq("2.12.10", "2.13.8")

def javaVersion = VersionNumber(sys.props("java.specification.version"))

def isJava(x: Integer) = javaVersion match {
  case VersionNumber(Seq(y, _*), _, _) => x == y
  case _                               => false
}

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

val fgrep = InputKey[Unit]("fgrep")

fgrep := {
  val args: Seq[String] = Def.spaceDelimited().parsed

  val found = IO.readLines(file(args(1))).exists(_.contains(args(0)))

  if (found) {
    println(s"Found '${args(0)}' in '${args(1)}'")

  } else {
    sys.error(s"Failed to fgrep '${args(0)}' '${args(1)}'")
  }
}

val jgrep = InputKey[Unit]("jgrep")

jgrep := {
  val args: Seq[String] = Def.spaceDelimited().parsed

  val found = IO.readLines(file(args(1))).exists(regexMatches(args(0).r)(_))

  if (found) {
    println(s"Found '${args(0)}' in '${args(1)}'")

  } else {
    sys.error(s"Failed to jgrep '${args(0)}' '${args(1)}'")
  }
}
