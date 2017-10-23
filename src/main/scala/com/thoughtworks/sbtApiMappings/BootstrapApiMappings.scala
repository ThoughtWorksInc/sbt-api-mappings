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

  override def globalSettings: Seq[Def.Setting[_]] = Seq(
    bootstrapJavadocURL := defaultBootstrapJavadocUrl
  )

  private[sbtApiMappings] val defaultBootstrapJavadocUrl = {
    val javaVersion = sys.props("java.version").split('-')(0)
    new URL(raw"""https://docs.oracle.com/javase/$javaVersion/docs/api/index.html""")
  }

  override def projectSettings = Seq(Compile, Test).flatMap { config =>
    inConfig(config) {
      inTask(doc) {
        Seq(
          apiMappings ++= {
            val url = bootstrapJavadocURL.value
            val log = streams.value.log

            if (!ManagementFactory.getRuntimeMXBean.isBootClassPathSupported) {
              log.info("sbt-api-mappings: boot class path not supported, not adding it to apiMappings")
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
