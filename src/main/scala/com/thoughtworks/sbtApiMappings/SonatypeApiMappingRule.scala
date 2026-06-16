package com.thoughtworks.sbtApiMappings

import sbt._
import Compat._
import sbtcompat.PluginCompat
import sbt.internal.librarymanagement.mavenint.PomExtraDependencyAttributes

/** Mapping to sonatype.org URL for all artifacts */
object SonatypeApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = noTrigger

  private val JarBaseNameRegex = """(.*)\.jar""".r

  private def nonSbtModuleID: Attributed[PluginCompat.FileRef] => Option[
    (String, String, String, String)
  ] = { jar =>
    // `data.name` is `File.getName` on sbt 1.x (via sbt's RichFile) and
    // `HashedVirtualFileRef.name` on sbt 2.x.
    jar.data.name match {
      case JarBaseNameRegex(baseName) =>
        for {
          moduleID <- jar
            .get(PluginCompat.moduleIDStr)
            .map(PluginCompat.parseModuleIDStrAttribute)
          if !moduleID.extraAttributes.contains(
            PomExtraDependencyAttributes.SbtVersionKey
          )
        } yield (
          baseName,
          moduleID.organization,
          moduleID.name,
          moduleID.revision
        )
      case _ =>
        None
    }
  }

  private def sonatypeRule
      : PartialFunction[Attributed[PluginCompat.FileRef], Compat.DocUrl] = {
    case nonSbtModuleID.extract(
          baseName,
          organization,
          libraryName,
          revision
        ) =>
      val organizationPath = organization.replace('.', '/')
      url(
        s"https://oss.sonatype.org/service/local/repositories/public/archive/$organizationPath/$libraryName/$revision/$baseName-javadoc.jar/!/"
      )
  }

  override def projectSettings = {
    apiMappingRules := apiMappingRules.value.orElse(sonatypeRule)
  }

}
