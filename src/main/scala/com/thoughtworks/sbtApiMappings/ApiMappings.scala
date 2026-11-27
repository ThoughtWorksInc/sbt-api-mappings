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
import Compatibility.Extractor._
import sbt.plugins.JvmPlugin
import sbtcompat.PluginCompat
// Brings `Def.uncached`, which sbt2-compat backfills on sbt 1.x (native on 2.x).
import sbtcompat.PluginCompat._

object ApiMappings extends AutoPlugin {

  object autoImport {
    val apiMappingRules =
      SettingKey[
        PartialFunction[Attributed[PluginCompat.FileRef], Compatibility.URL]
      ](
        "api-mapping-rules",
        "Rules to create api-mappings"
      )
  }
  import autoImport._

  override def requires: Plugins = JvmPlugin

  override def trigger = allRequirements

  override def globalSettings = {
    apiMappingRules := PartialFunction.empty
  }

  override def projectSettings = Seq(Compile, Test).flatMap { config =>
    Seq(
      config / doc / autoAPIMappings := true,
      config / doc / apiMappings ++= Def.uncached {
        val rules = (config / doc / apiMappingRules).value
        val existingMappings = (config / doc / apiMappings).value
        (config / doc / dependencyClasspath).value.view.collect {
          case jar @ rules.extract(url)
              if !existingMappings.exists(_._1 == jar.data) =>
            jar.data -> url
        }.toMap
      }
    )
  }

}

// vim: set ts=2 sw=2 et:
