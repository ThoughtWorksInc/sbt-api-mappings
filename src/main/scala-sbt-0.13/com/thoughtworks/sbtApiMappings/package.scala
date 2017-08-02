package com.thoughtworks.sbtApiMappings

object `package` {

  object ModuleID {
    def unapply(m: sbt.ModuleID): Some[(String, String, String)] =
      Some((m.organization, m.name, m.revision))
  }

}
