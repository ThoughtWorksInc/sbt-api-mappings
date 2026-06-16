package com.thoughtworks.sbtApiMappings

import sbt._
import Compatibility.Extractor._
import sbtcompat.PluginCompat

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object PlayApiMappingRule extends AutoPlugin {

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

  private def playRule
      : PartialFunction[Attributed[PluginCompat.FileRef], Compatibility.URL] = {
    case moduleID.extract(
          "com.typesafe.play",
          libraryName,
          VersionNumber(Seq(majorVersion, minorVersion, _*), _, _)
        ) if libraryName == "play" || libraryName.startsWith("play-") =>
      url(
        s"https://playframework.com/documentation/$majorVersion.$minorVersion.x/api/scala/"
      )
  }

  override def projectSettings = {
    apiMappingRules := playRule.orElse(apiMappingRules.value)
  }

}
