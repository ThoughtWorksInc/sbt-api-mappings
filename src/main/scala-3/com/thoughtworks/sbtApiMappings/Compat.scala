package com.thoughtworks.sbtApiMappings

import java.io.File
import java.net.URI

import sbt._
import sbtcompat.PluginCompat
import xsbti.HashedVirtualFileRef

/** sbt 2.x specific bindings: only the pieces that genuinely differ from sbt
  * 1.x. Everything that sbt2-compat can express uniformly is plain shared code.
  */
object Compat:

  /** The value type of the `apiMappings` map. */
  type DocUrl = URI

  /** Convert a real [[java.io.File]] (e.g. a JDK bootstrap classpath jar) to an
    * `apiMappings` key.
    *
    * The bootstrap class loader entries (e.g. the synthetic `/modules/java.base`
    * on Java 9+) are not real files, so they cannot go through `fileConverter`
    * (which validates that the file exists). Build the reference directly.
    */
  def fileToDocKey: Def.Initialize[Task[File => PluginCompat.FileRef]] =
    Def.task { (file: File) =>
      HashedVirtualFileRef.of(file.toPath.toString, ""): PluginCompat.FileRef
    }

  // On Scala 3 a PartialFunction is already a valid pattern, so `.extract` turns
  // a function (or partial function) into one.

  extension [A, B](f: A => Option[B])
    def extract: PartialFunction[A, B] = Function.unlift(f)

  extension [A, B](pf: PartialFunction[A, B])
    def extract: PartialFunction[A, B] = pf
