package com.thoughtworks.sbtApiMappings

import sbt.{AutoPlugin, ModuleID, VersionNumber, _}
import Ordering.Implicits._
import Compat.Extractor._
import sbtcompat.PluginCompat

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object ScalaApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def moduleID
      : Attributed[PluginCompat.FileRef] => Option[(String, String, String)] =
    _.get(PluginCompat.moduleIDStr)
      .map(PluginCompat.parseModuleIDStrAttribute)
      .map { moduleID =>
        (moduleID.organization, moduleID.name, moduleID.revision)
      }

  private def scalaRule
      : PartialFunction[Attributed[PluginCompat.FileRef], Compat.DocUrl] = {
    case moduleID.extract("org.scala-lang", "scala-library", revision) =>
      url(s"http://scala-lang.org/files/archive/api/$revision/")
    case moduleID.extract("org.scala-lang", libraryName, revision)
        // Scala web-site only contains API documentation for specific libraries after version 2.11.0
        if libraryName.startsWith("scala-") && VersionNumber(
          revision
        ).numbers >= Seq(2, 11, 0) =>
      url(
        s"http://scala-lang.org/files/archive/api/$revision/$libraryName/"
      )
  }

  override def projectSettings = {
    apiMappingRules := scalaRule.orElse(apiMappingRules.value)
  }

}
