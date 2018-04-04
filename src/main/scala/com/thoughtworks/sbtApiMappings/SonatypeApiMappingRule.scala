package com.thoughtworks.sbtApiMappings

import sbt._
import com.thoughtworks.Extractor._
import sbt.internal.librarymanagement.mavenint.PomExtraDependencyAttributes

/** Mapping to sonatype.org URL for all artifacts */
object SonatypeApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = noTrigger

  private val JarBaseNameRegex = """(.*)\.jar""".r

  private def nonSbtModuleID: Attributed[File] => Option[(String,String, String, String)] = { jar =>
    jar.data.getName match {
      case JarBaseNameRegex(baseName) =>
        for {
          moduleID <- jar.get(Keys.moduleID.key)
          if !moduleID.extraAttributes.contains(PomExtraDependencyAttributes.SbtVersionKey)
        } yield (baseName, moduleID.organization, moduleID.name, moduleID.revision)
      case _ =>
        None
    }
  }

  private def sonatypeRule: PartialFunction[Attributed[File], URL] = {
    case nonSbtModuleID.extract(baseName, organization, libraryName, revision) =>
      val organizationPath = organization.replace('.', '/')
      url(
        s"https://oss.sonatype.org/service/local/repositories/public/archive/$organizationPath/$libraryName/$revision/$baseName-javadoc.jar/!/index.html")
  }

  override def projectSettings = {
    apiMappingRules := apiMappingRules.value.orElse(sonatypeRule)
  }

}
