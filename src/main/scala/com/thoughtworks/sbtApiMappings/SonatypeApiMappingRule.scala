package com.thoughtworks.sbtApiMappings

import sbt._
import com.thoughtworks.Extractor._

object SonatypeApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private val JarBaseNameRegex = """(.*)\.jar""".r

  private def baseNameAndModuleID: Attributed[File] => Option[(String, String, String, String)] = { jar =>
    jar.data.getName match {
      case JarBaseNameRegex(baseName) =>
        jar.get(Keys.moduleID.key).map { moduleID =>
          (baseName, moduleID.organization, moduleID.name, moduleID.revision)
        }
      case _ =>
        None
    }
  }

  private def sonatypeRule: PartialFunction[Attributed[File], URL] = {
    case baseNameAndModuleID.extract(baseName, organization, libraryName, revision) =>
      val organizationPath = organization.replace('.', '/')
      url(s"https://oss.sonatype.org/service/local/repositories/public/archive/$organizationPath/$libraryName/$revision/$baseName-javadoc.jar/!/index.html")
  }

  override def projectSettings = {
    apiMappingRules := apiMappingRules.value.orElse(sonatypeRule)
  }

}
