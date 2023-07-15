package com.thoughtworks.sbtApiMappings

import sbt._
import com.thoughtworks.Extractor._
import sbt.internal.librarymanagement.mavenint.PomExtraDependencyAttributes

object JavadocIoApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def nonSbtModuleID
      : Attributed[File] => Option[(String, String, String)] = { jar =>
    for {
      moduleID <- jar.get(Keys.moduleID.key)
      if !moduleID.extraAttributes.contains(
        PomExtraDependencyAttributes.SbtVersionKey
      )
    } yield (moduleID.organization, moduleID.name, moduleID.revision)
  }

  private def javadocIoRule: PartialFunction[Attributed[File], URL] = {
    case nonSbtModuleID.extract(organization, libraryName, revision) =>
      val organizationPath = organization.replace('.', '/')
      url(s"https://javadoc.io/page/$organization/$libraryName/$revision/")
  }

  override def projectSettings = {
    apiMappingRules := apiMappingRules.value.orElse(javadocIoRule)
  }

}
