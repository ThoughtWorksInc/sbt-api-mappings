val check = TaskKey[Unit]("check")

def regexMatches(matcher: scala.util.matching.Regex)(str: String): Boolean = {
  matcher.findFirstIn(str).isDefined
}

check := {

  val log = sLog.value

  val compileApiMappings = (apiMappings in Compile in doc).value

  val expect = "https://docs.oracle.com/javase/\\d/docs/api/index.html".r

  val found = compileApiMappings.values.exists { url =>
    regexMatches(expect)(url.toString)
  }

  if (found) {
    log.info("Found javadoc url in apiMappings")

  } else {
    log.info("Entries in apiMappings:")
    compileApiMappings.values.map("+ " + _.toString).foreach(
      log.info(_)
    )
    sys.error(s"Failed to match ${expect}")
  }
}

val fgrep = InputKey[Unit]("fgrep")

fgrep := {
  val args: Seq[String] = Def.spaceDelimited().parsed

  val log = sLog.value

  val found = IO.readLines(file(args(1))).exists(_.contains(args(0)))

  if (found) {
    log.info(s"Found '${args(0)}' in '${args(1)}'")

  } else {
    sys.error(s"Failed to fgrep '${args(0)}' '${args(1)}'")
  }
}

val jgrep = InputKey[Unit]("jgrep")

jgrep := {
  val args: Seq[String] = Def.spaceDelimited().parsed

  val log = sLog.value

  val found = IO.readLines(file(args(1))).exists(regexMatches(args(0).r)(_))

  if (found) {
    log.info(s"Found '${args(0)}' in '${args(1)}'")

  } else {
    sys.error(s"Failed to jgrep '${args(0)}' '${args(1)}'")
  }
}
