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

object ApiMappings extends AutoPlugin {

  private val ScalaLibraryRegex = """^.*[/\\]scala-library-([\d\.]+).jar$""".r

  private val IvyRegex = """^.*[/\\]([\.\-_\w]+)[/\\]([\.\-_\w]+)[/\\](?:jars|bundles)[/\\]([\.\-_\w]+)\.jar$""".r

  private val PlayRegex = """^.*[/\\]play(-[\w\-]+)?(_[\d\.]+)?-([\d\.]+).jar$""".r

  override final def trigger = allRequirements

  override final lazy val projectSettings = Seq(
    autoAPIMappings := true,
    apiMappings ++= {
      (for {
        jar <- (dependencyClasspath in Compile).value
        fullyFile = jar.data
        urlOption = fullyFile.getCanonicalPath match {
          case ScalaLibraryRegex(v) => {
            Some(url(raw"""http://scala-lang.org/files/archive/api/$v/index.html"""))
          }
          case PlayRegex(_, _, version) => {
            Some(url(raw"""https://playframework.com/documentation/$version/api/scala/index.html"""))
          }
          case IvyRegex(organization, name, jarBaseFile) if jarBaseFile.startsWith(s"$name-") => {
            val version = jarBaseFile.substring(name.length + 1, jarBaseFile.length)
            Some(url(raw"""https://oss.sonatype.org/service/local/repositories/releases/archive/${organization.replace('.', '/')}/$name/$version/$jarBaseFile-javadoc.jar/!/index.html"""))
          }
          case _ => {
            None
          }
        }
        url <- urlOption
      } yield (fullyFile -> url))(collection.breakOut(Map.canBuildFrom))
    })

}

// vim: set ts=2 sw=2 et:
