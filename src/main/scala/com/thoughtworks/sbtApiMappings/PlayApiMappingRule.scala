package com.thoughtworks.sbtApiMappings

import sbt._

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object PlayApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def playRule: PartialFunction[ModuleID, URL] = {
    case ModuleID("com.typesafe.play", libraryName, VersionNumber(Seq(majorVersion, minorVersion, _ *), _, _), _, _, _, _, _, _, _, _)
        if libraryName == "play" || libraryName.startsWith("play-") =>
      url(s"https://playframework.com/documentation/$majorVersion.$minorVersion.x/api/scala/index.html")
  }

  override def projectSettings = {
    apiMappingRules := playRule.orElse(apiMappingRules.value)
  }

}
