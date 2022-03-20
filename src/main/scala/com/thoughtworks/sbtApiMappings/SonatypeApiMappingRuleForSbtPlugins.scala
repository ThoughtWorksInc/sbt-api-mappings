package com.thoughtworks.sbtApiMappings

import sbt._
import com.thoughtworks.Extractor._
import sbt.internal.librarymanagement.mavenint.PomExtraDependencyAttributes

/** Mapping to sonatype.org URL only for artifacts that are not supported by
  * javadoc.io
  */
object SonatypeApiMappingRuleForSbtPlugins extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private val JarBaseNameRegex = """(.*)\.jar""".r

  private def sbtModuleID: Attributed[File] => Option[
    (String, String, String, String, String, String)
  ] = { jar =>
    jar.data.getName match {
      case JarBaseNameRegex(baseName) =>
        for {
          moduleID <- jar.get(Keys.moduleID.key)
          sbtVerion <- moduleID.extraAttributes.get(
            PomExtraDependencyAttributes.SbtVersionKey
          )
          scalaVerion <- moduleID.extraAttributes.get(
            PomExtraDependencyAttributes.ScalaVersionKey
          )
        } yield (
          baseName,
          moduleID.organization,
          moduleID.name,
          moduleID.revision,
          scalaVerion,
          sbtVerion
        )
      case _ =>
        None
    }
  }

  private def sonatypeRule: PartialFunction[Attributed[File], URL] = {
    case sbtModuleID.extract(
          baseName,
          organization,
          libraryName,
          revision,
          scalaVersion,
          sbtVersion
        ) =>
      val organizationPath = organization.replace('.', '/')
      url(
        s"https://oss.sonatype.org/service/local/repositories/public/archive/$organizationPath/${libraryName}_${scalaVersion}_${sbtVersion}/$revision/$baseName-javadoc.jar/!/"
      )
  }

  override def projectSettings = {
    apiMappingRules := apiMappingRules.value.orElse(sonatypeRule)
  }

}
