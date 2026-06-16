package com.thoughtworks.sbtApiMappings

import sbt._
import Compat.Extractor._
import sbtcompat.PluginCompat
import sbt.internal.librarymanagement.mavenint.PomExtraDependencyAttributes

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
          PomExtraDependencyAttributes.SbtVersionKey
        )
      } yield (moduleID.organization, moduleID.name, moduleID.revision)
  }

  private def javadocIoRule
      : PartialFunction[Attributed[PluginCompat.FileRef], Compat.DocUrl] = {
    case nonSbtModuleID.extract(organization, libraryName, revision) =>
      url(
        s"https://javadoc.io/page/$organization/$libraryName/$revision/"
      )
  }

  override def projectSettings = {
    apiMappingRules := apiMappingRules.value.orElse(javadocIoRule)
  }

}
