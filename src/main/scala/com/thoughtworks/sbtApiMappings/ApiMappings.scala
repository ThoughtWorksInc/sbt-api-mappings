/*
Copyright 2015 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.thoughtworks.sbtApiMappings

import sbt._
import Keys._
import com.thoughtworks.Extractor._
import scala.Ordering.Implicits._

object ApiMappings extends AutoPlugin {

  override final def trigger = allRequirements

  override final lazy val projectSettings = Seq(
    autoAPIMappings := true,
    apiMappings ++= {
      val getModuleID = { attributed: Attributed[File] =>
        attributed.get(moduleID.key)
      }
      val jars = ((dependencyClasspath in Compile in doc).value ++ (dependencyClasspath in Test in doc).value).distinct
      jars.collect {
        case jar @ getModuleID.extract(
              ModuleID("org.scala-lang", "scala-library", revision, _, _, _, _, _, _, _, _)) =>
          jar.data -> url(s"http://scala-lang.org/files/archive/api/$revision/index.html")
        case jar @ getModuleID.extract(ModuleID("org.scala-lang", libraryName, revision, _, _, _, _, _, _, _, _))
            // Scala web-site only contains API documentation for specific libraries after version 2.11.0
            if libraryName.startsWith("scala-") && VersionNumber(revision).numbers >= Seq(2, 11, 0) =>
          jar.data -> url(s"http://scala-lang.org/files/archive/api/$revision/$libraryName/index.html")
        case jar @ getModuleID.extract(
              ModuleID(
                "com.typesafe.play",
                libraryName,
                VersionNumber(Seq(majorVersion, minorVersion, _ *), _, _),
                _,
                _,
                _,
                _,
                _,
                _,
                _,
                _
              )
            ) if libraryName == "play" || libraryName.startsWith("play-") =>
          jar.data -> url(
            s"https://playframework.com/documentation/$majorVersion.$minorVersion.x/api/scala/index.html")
        case jar @ getModuleID.extract(ModuleID("org.apache.spark", _, revision, _, _, _, _, _, _, _, _)) =>
          jar.data -> url(raw"""https://spark.apache.org/docs/$revision/api/scala/index.html""")
        case jar @ getModuleID.extract(ModuleID(organization, libraryName, revision, _, _, _, _, _, _, _, _))
            if !apiMappings.value.contains(jar.data) =>
          jar.data -> url(raw"""https://oss.sonatype.org/service/local/repositories/public/archive/${organization
            .replace('.', '/')}/$libraryName/$revision/$libraryName-$revision-javadoc.jar/!/index.html""")
      }.toMap
    }
  )

}

// vim: set ts=2 sw=2 et:
