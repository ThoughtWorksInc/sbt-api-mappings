package com.thoughtworks.sbtApiMappings

import sbt.{AutoPlugin, _}

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object SparkApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def sparkRule: PartialFunction[ModuleID, URL] = {
    case ModuleID("org.apache.spark", _, revision) =>
      url(s"https://spark.apache.org/docs/$revision/api/scala/index.html")
  }

  override def projectSettings = {
    apiMappingRules := sparkRule.orElse(apiMappingRules.value)
  }

}
