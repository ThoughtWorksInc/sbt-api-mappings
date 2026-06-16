val check = TaskKey[Unit]("check")

val scalacheckModuleId = "org.scalacheck" %% "scalacheck" % "1.14.3"

// The classpath entry key is a File on sbt 1.x and a HashedVirtualFileRef on
// sbt 2.x; both render the jar path/name in toString, so look entries up by a
// substring of toString to stay cross-compatible.
def findByName(mappings: Map[_, _], jarName: String): Option[String] =
  mappings.collectFirst {
    case (key, url) if key.toString.contains(jarName) => url.toString
  }

check := {
  // The expected URL is browsable but not accessible from JRE 8's java.net.HttpURLConnection
  val expectedScaladocUrl =
    "https://www.scala-lang.org/api/2.13.1/"

  assert(
    findByName(
      (Compile / doc / apiMappings).value,
      "scala-library"
    ).contains(expectedScaladocUrl)
  )
  assert(
    findByName(
      (Test / doc / apiMappings).value,
      "scala-library"
    ).contains(expectedScaladocUrl)
  )

  val scalacheckJarName = Artifact.artifactName(
    ScalaVersion(scalaVersion.value, scalaBinaryVersion.value),
    scalacheckModuleId,
    Artifact("scalacheck")
  )

  val expectedUrl =
    "https://javadoc.io/page/org.scalacheck/scalacheck_2.13/1.14.3/"
  assert(
    findByName((Test / doc / apiMappings).value, scalacheckJarName)
      .contains(expectedUrl)
  )
  assert(
    findByName((Compile / doc / apiMappings).value, scalacheckJarName).isEmpty
  )
}

Global / scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.13.1")

libraryDependencies += scalacheckModuleId % Test

Test / sources += baseDirectory.value / "test_renamed" / "Test.scala"
