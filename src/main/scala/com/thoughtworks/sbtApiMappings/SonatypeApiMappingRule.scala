package com.thoughtworks.sbtApiMappings

import sbt._

object SonatypeApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def sonatypeRule: PartialFunction[ModuleID, URL] = {
    case module @ ModuleID(organization, libraryName, revision) =>
      val organizationPath = organization.replace('.', '/')
      url(s"https://oss.sonatype.org/service/local/repositories/public/archive/$organizationPath/$libraryName/$revision/$libraryName-$revision-javadoc.jar/!/index.html")
  }

  override def projectSettings = {
    apiMappingRules := apiMappingRules.value.orElse(sonatypeRule)
  }

}
