val check = TaskKey[Unit]("check")

val scalacheckModuleId = "org.scalacheck" %% "scalacheck" % "1.18.1"

// The classpath entry key is a File on sbt 1.x and a HashedVirtualFileRef on
// sbt 2.x; both render the jar path/name in toString, so look entries up by a
// substring of toString to stay cross-compatible.
def findByName(mappings: Map[_, _], jarName: String): Option[String] =
  mappings.collectFirst {
    case (key, url) if key.toString.contains(jarName) => url.toString
  }

check := {
  val compileMappings = (Compile / doc / apiMappings).value
  val testMappings = (Test / doc / apiMappings).value

  // scala-library should map to a scala-lang.org scaladoc URL for this version.
  // The exact URL differs by sbt version (autoAPIMappings provides
  // https://www.scala-lang.org/api/<v>/ on sbt 1.x, while on sbt 2.x this
  // plugin's ScalaApiMappingRule provides http://scala-lang.org/files/archive/
  // api/<v>/), so assert leniently.
  def assertScalaLibrary(mappings: Map[_, _]): Unit =
    assert(
      findByName(mappings, "scala-library").exists { url =>
        url.contains("scala-lang.org") && url.contains(scalaVersion.value)
      },
      s"scala-library scaladoc mapping not found in $mappings"
    )
  assertScalaLibrary(compileMappings)
  assertScalaLibrary(testMappings)

  // scalacheck (a Test-only dependency) should map to a javadoc.io URL. As with
  // scala-library the exact form differs by sbt version (autoAPIMappings reads
  // https://www.javadoc.io/doc/<...> from the POM on sbt 1.x, while on sbt 2.x
  // this plugin's JavadocIoApiMappingRule provides https://javadoc.io/page/<...>),
  // so assert leniently.
  val scalacheckJarName = Artifact.artifactName(
    ScalaVersion(scalaVersion.value, scalaBinaryVersion.value),
    scalacheckModuleId,
    Artifact("scalacheck")
  )
  assert(
    findByName(testMappings, scalacheckJarName).exists { url =>
      url.contains("javadoc.io") && url.contains("scalacheck") &&
      url.contains("1.18.1")
    },
    s"scalacheck javadoc mapping not found in $testMappings"
  )
  assert(
    findByName(compileMappings, scalacheckJarName).isEmpty,
    s"scalacheck should not be on the Compile classpath: $compileMappings"
  )
}

Global / scalaVersion := "2.13.16"

crossScalaVersions := Seq("2.13.16")

libraryDependencies += scalacheckModuleId % Test

Test / sources += baseDirectory.value / "test_renamed" / "Test.scala"
