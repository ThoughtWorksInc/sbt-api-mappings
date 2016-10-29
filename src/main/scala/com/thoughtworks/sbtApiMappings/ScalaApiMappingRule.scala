package com.thoughtworks.sbtApiMappings

import sbt.{AutoPlugin, ModuleID, VersionNumber, _}
import Ordering.Implicits._

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object ScalaApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def scalaRule: PartialFunction[ModuleID, URL] = {
    case ModuleID("org.scala-lang", "scala-library", revision, _, _, _, _, _, _, _, _) =>
      url(s"http://scala-lang.org/files/archive/api/$revision/index.html")
    case ModuleID("org.scala-lang", libraryName, revision, _, _, _, _, _, _, _, _)
      // Scala web-site only contains API documentation for specific libraries after version 2.11.0
      if libraryName.startsWith("scala-") && VersionNumber(revision).numbers >= Seq(2, 11, 0) =>
      url(s"http://scala-lang.org/files/archive/api/$revision/$libraryName/index.html")

  }

  override def projectSettings = {
    apiMappingRules := scalaRule.orElse(apiMappingRules.value)
  }

}
