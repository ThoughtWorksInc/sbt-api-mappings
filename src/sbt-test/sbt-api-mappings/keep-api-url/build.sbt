scalaVersion := "2.13.1"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.1"

val check = TaskKey[Unit]("check")
check := {
  val result = (Compile / doc / apiMappings).value.collectFirst {
    case (file, url) if (file.getAbsolutePath.contains("akka-actor")) => url.getHost
  }

  result match {
    case Some(host) if host.contains("akka.io") => // ok
    case Some(host) => sys.error(s"API URL in akka-actor has been changed. Found: [$host]")
    case None => sys.error("No API URL found for akka-actor")
  }
}
