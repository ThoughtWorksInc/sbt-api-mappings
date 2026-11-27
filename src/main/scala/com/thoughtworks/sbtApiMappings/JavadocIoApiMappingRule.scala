package com.thoughtworks.sbtApiMappings

import sbt._
import Compatibility.Extractor._
import sbtcompat.PluginCompat

object JavadocIoApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def nonSbtModuleID
      : Attributed[PluginCompat.FileRef] => Option[(String, String, String)] = {
    jar =>
      for {
        moduleID <- jar
          .get(PluginCompat.moduleIDStr)
          .map(PluginCompat.parseModuleIDStrAttribute)
        if !moduleID.extraAttributes.contains(
          Compatibility.sbtVersionAttribute
        )
      } yield (moduleID.organization, moduleID.name, moduleID.revision)
  }

  private def javadocIoRule
      : PartialFunction[Attributed[PluginCompat.FileRef], Compatibility.URL] = {
    case nonSbtModuleID.extract(organization, libraryName, revision) =>
      url(
        s"https://javadoc.io/page/$organization/$libraryName/$revision/"
      )
  }

  override def projectSettings = {
    apiMappingRules := apiMappingRules.value.orElse(javadocIoRule)
  }

}
