package com.thoughtworks.sbtApiMappings

import java.io.File
import java.lang.management.ManagementFactory

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
// Brings `Def.uncached`, which sbt2-compat backfills on sbt 1.x (native on 2.x).
import sbtcompat.PluginCompat._

/** API mappings for classpath used by the bootstrap class loader.
  * @author
  *   杨博 (Yang Bo)
  */
object BootstrapApiMappings extends AutoPlugin {
  override def requires: Plugins = JvmPlugin

  override def trigger = allRequirements

  object autoImport {
    val bootstrapJavadocURL =
      SettingKey[Compat.DocUrl](
        "bootstrap-javadoc-url",
        "Javadoc URL for classpath used by bootstrap class loader"
      )
  }

  import autoImport._

  private[sbtApiMappings] def defaultBootstrapJavadocUrl = {
    val javaVersion = sys.props("java.specification.version") match {
      case VersionNumber(Seq(1, javaVersion, _*), _, _) =>
        javaVersion // 1.6-1.8
      case VersionNumber(Seq(javaVersion, _*), _, _) => javaVersion // 9+
      case _                                         => 8
    }
    if (javaVersion >= 11) {
      url(
        s"https://docs.oracle.com/en/java/javase/${javaVersion}/docs/api/java.base/"
      )
    } else {
      url(s"https://docs.oracle.com/javase/${javaVersion}/docs/api/")
    }
  }

  override def globalSettings = Seq(
    bootstrapJavadocURL := defaultBootstrapJavadocUrl
  )

  override def projectSettings = Seq(Compile, Test).flatMap { config =>
    Seq(
      config / doc / apiMappings ++= Def.uncached {
        val javadocUrl = bootstrapJavadocURL.value
        val toDocKey = Compat.fileToDocKey.value
        val log = streams.value.log

        if (!ManagementFactory.getRuntimeMXBean.isBootClassPathSupported) {
          // Copied from scala-js/project/Build.scala for Java 9 or later
          Map(toDocKey(file("/modules/java.base")) -> javadocUrl)
        } else {
          ManagementFactory.getRuntimeMXBean.getBootClassPath
            .split(File.pathSeparatorChar)
            .view
            .map { jar =>
              toDocKey(new File(jar)) -> javadocUrl
            }
            .toMap
        }
      }
    )
  }

}
