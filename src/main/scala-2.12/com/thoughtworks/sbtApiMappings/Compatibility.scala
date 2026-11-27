package com.thoughtworks.sbtApiMappings

import java.io.File

import sbt._
import sbtcompat.PluginCompat

/** sbt 1.x specific bindings: only the pieces that genuinely differ from sbt
  * 2.x. Everything that sbt2-compat can express uniformly is plain shared code.
  */
private[sbtApiMappings] object Compatibility {

  /** The value type of the `apiMappings` map. */
  type URL = java.net.URL

  /** Convert a real [[java.io.File]] (e.g. a JDK bootstrap classpath jar) to an
    * `apiMappings` key. On sbt 1.x the key is already a `File`.
    */
  def fileToDocKey: Def.Initialize[Task[File => PluginCompat.FileRef]] =
    Def.task { (file: File) => file }

  /** The `.extract` pattern support, opted into with
    * `import Compatibility.Extractor._`. On Scala 2.12 it simply re-exports the
    * `com.thoughtworks.extractor` library.
    */
  val Extractor = com.thoughtworks.Extractor
}
