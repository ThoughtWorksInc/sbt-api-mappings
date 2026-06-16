package com.thoughtworks.sbtApiMappings

import sbt.{AutoPlugin, ModuleID, _}
import Compatibility.Extractor._
import sbtcompat.PluginCompat

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object SparkApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def moduleID
      : Attributed[PluginCompat.FileRef] => Option[(String, String, String)] =
    _.get(PluginCompat.moduleIDStr)
      .map(PluginCompat.parseModuleIDStrAttribute)
      .map { moduleID =>
        (moduleID.organization, moduleID.name, moduleID.revision)
      }

  private def sparkRule
      : PartialFunction[Attributed[PluginCompat.FileRef], Compatibility.DocUrl] = {
    case moduleID.extract("org.apache.spark", _, revision) =>
      url(s"https://spark.apache.org/docs/$revision/api/scala/")
  }

  override def projectSettings = {
    apiMappingRules := sparkRule.orElse(apiMappingRules.value)
  }

}
