# sbt-api-mappings

[![Build Status](https://travis-ci.org/ThoughtWorksInc/sbt-api-mappings.svg?branch=master)](https://travis-ci.org/ThoughtWorksInc/sbt-api-mappings)

**sbt-api-mappings** is a Sbt plugin that fills apiMappings for common Scala libraries.

## Motivation

Sometimes when you wrote ScalaDoc for your own classes, you may want to reference to documentation in some other libraries.

For example:

```
/**
 * My own class, which has two friend, [[scala.Option]] and [[scalaz.Monad]].
 */
class MyClass
```

Unfortunately when use run `doc` command in Sbt, you will receive a warning and the link would not be created.

```
/path/to/MyClass.scala:3: Could not find any member to link for "scala.Option".
```

This plugin resolve this problem.

### Usage

#### Step 1: Add the following lines in your `project/plugins.sbt`:

```
lazy val root = project in file(".") dependsOn `sbt-api-mappings`

lazy val `sbt-api-mappings` = RootProject(uri("https://github.com/ThoughtWorksInc/sbt-api-mappings.git"))
```

#### Step 2: And add the following lines in your `build.sbt`:

```
enablePlugins(ApiMappings)
```

#### Step 3: Relead the Sbt configuration:

```
> reload
```

#### Step 4: Generate you API documentation:

```
> doc
```

Now, open the API documentation in your browser, and you will find links to the `scala.Option` and `scalaz.Monad`'s documentation in your `MyClass` page.
