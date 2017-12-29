package com.thoughtworks.sbtApiMappings

import java.io.File
import java.lang.management.ManagementFactory
import java.net.URL

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

/** API mappings for classpath used by the bootstrap class loader.
  * @author 杨博 (Yang Bo)
  */
object BootstrapApiMappings extends AutoPlugin {
  override def requires: Plugins = JvmPlugin

  override def trigger = allRequirements

  object autoImport {
    val bootstrapJavadocURL =
      SettingKey[URL]("bootstrap-javadoc-url", "Javadoc URL for classpath used by bootstrap class loader")
  }

  import autoImport._

  private[sbtApiMappings] def defaultBootstrapJavadocUrl = {
    val javaVersion = sys.props("java.specification.version") match {
      case VersionNumber(Seq(1, javaVersion, _*), _, _) => javaVersion // 1.6-1.8
      case VersionNumber(Seq(javaVersion, _*),    _, _) => javaVersion // 9+
      case specificationVersion =>
        specificationVersion
    }
    url(s"https://docs.oracle.com/javase/${javaVersion}/docs/api/index.html")
  }

  override def globalSettings: Seq[Def.Setting[_]] = Seq(
    bootstrapJavadocURL := defaultBootstrapJavadocUrl
  )

  override def projectSettings = Seq(Compile, Test).flatMap { config =>
    inConfig(config) {
      inTask(doc) {
        Seq(
          apiMappings ++= {
            val url = bootstrapJavadocURL.value
            val log = streams.value.log

            if (!ManagementFactory.getRuntimeMXBean.isBootClassPathSupported) {
              // FIXME: Java 9 now uses modules and urls rather than jars and files.
              log.warn("sbt-api-mappings: bootstrap class path is not supported, unable to add JavaDoc for JavaSE to apiMappings")
              Map.empty
            } else {
              ManagementFactory.getRuntimeMXBean.getBootClassPath
                .split(File.pathSeparatorChar)
                .map { jar =>
                  new File(jar) -> url
                }(collection.breakOut(Map.canBuildFrom))
            }
          }
        )
      }
    }
  }

}
