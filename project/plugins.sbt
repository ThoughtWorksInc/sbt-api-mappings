libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

// Use jgit 5.x because newer version does not support Java 8, while our CI can run on Java 8
dependencyOverrides += "org.eclipse.jgit" % "org.eclipse.jgit" % "7.8.0.202609011348-r"
