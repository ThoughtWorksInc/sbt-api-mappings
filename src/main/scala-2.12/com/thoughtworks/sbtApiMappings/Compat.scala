package com.thoughtworks.sbtApiMappings

import java.io.File
import java.net.URL

import sbt._
import sbtcompat.PluginCompat

/** sbt 1.x specific bindings: only the pieces that genuinely differ from sbt
  * 2.x. Everything that sbt2-compat can express uniformly is plain shared code.
  */
object Compat {

  /** The value type of the `apiMappings` map. */
  type DocUrl = URL

  /** Convert a real [[java.io.File]] (e.g. a JDK bootstrap classpath jar) to an
    * `apiMappings` key. On sbt 1.x the key is already a `File`.
    */
  def fileToDocKey: Def.Initialize[Task[File => PluginCompat.FileRef]] =
    Def.task { (file: File) => file }

  // On Scala 2.12 a function/partial function cannot be used as a pattern
  // directly, so `.extract` adapts it into an extractor object.

  final class Extractor[A, B](unlifted: A => Option[B]) {
    def unapply(a: A): Option[B] = unlifted(a)
  }

  implicit final class FunctionExtractOps[A, B](private val f: A => Option[B])
      extends AnyVal {
    def extract: Extractor[A, B] = new Extractor(f)
  }

  implicit final class PartialFunctionExtractOps[A, B](
      private val pf: PartialFunction[A, B]
  ) extends AnyVal {
    def extract: Extractor[A, B] = new Extractor(pf.lift)
  }

}
