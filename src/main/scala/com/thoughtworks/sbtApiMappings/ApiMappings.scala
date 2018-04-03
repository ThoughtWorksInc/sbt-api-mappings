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
import sbt.plugins.JvmPlugin

object ApiMappings extends AutoPlugin {

  object autoImport {
    val apiMappingRules =
      SettingKey[PartialFunction[ModuleID, URL]]("api-mapping-rules", "Rules to create api-mappings")
  }
  import autoImport._

  override def requires: Plugins = JvmPlugin

  override def trigger = allRequirements

  override def globalSettings = {
    apiMappingRules := PartialFunction.empty
  }

  override def projectSettings = Seq(Compile, Test).flatMap { config =>
    inConfig(config) {
      inTask(doc) {
        Seq(
          autoAPIMappings := true,
          apiMappings ++= {
            val getModuleID = { attributed: Attributed[File] =>
              attributed.get(moduleID.key)
            }
            val rules = apiMappingRules.value
            dependencyClasspath.value.collect {
              case jar @ getModuleID.extract(rules.extract(url)) if !apiMappings.value.exists(_._1 == jar.data) =>
                jar.data -> url
            }(collection.breakOut(Map.canBuildFrom))
          }
        )
      }
    }
  }

}

// vim: set ts=2 sw=2 et:
