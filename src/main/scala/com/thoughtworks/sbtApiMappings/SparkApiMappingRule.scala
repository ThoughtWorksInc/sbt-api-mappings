package com.thoughtworks.sbtApiMappings

import sbt.{AutoPlugin, ModuleID, _}
import com.thoughtworks.Extractor._

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object SparkApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def moduleID: Attributed[File] => Option[(String, String, String)] =
    _.get(Keys.moduleID.key).map { moduleID =>
      (moduleID.organization, moduleID.name, moduleID.revision)
    }

  private def sparkRule: PartialFunction[Attributed[File], URL] = {
    case moduleID.extract("org.apache.spark", _, revision) =>
      url(s"https://spark.apache.org/docs/$revision/api/scala/")
  }

  override def projectSettings = {
    apiMappingRules := sparkRule.orElse(apiMappingRules.value)
  }

}
