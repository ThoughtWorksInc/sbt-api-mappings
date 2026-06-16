# sbt-api-mappings

[![Scala CI](https://github.com/ThoughtWorksInc/sbt-api-mappings/actions/workflows/scala.yml/badge.svg)](https://github.com/ThoughtWorksInc/sbt-api-mappings/actions/workflows/scala.yml)
[![Scaladoc](https://javadoc.io/badge/com.thoughtworks.sbt-api-mappings/sbt-api-mappings_2.12_1.0/latest.svg?label=scaladoc)](https://javadoc.io/page/com.thoughtworks.sbt-api-mappings/sbt-api-mappings_2.12_1.0/12/com/thoughtworks/sbtApiMappings/index.html)
[![Latest version](https://index.scala-lang.org/thoughtworksinc/sbt-api-mappings/latest.svg)](https://index.scala-lang.org/thoughtworksinc/sbt-api-mappings)

**sbt-api-mappings** is a sbt plugin that fills `apiMappings` for common Scala libraries.

## Motivation

Sometimes when you wrote ScalaDoc for your own classes, you may want to reference to documentation in some other libraries.

For example:

``` scala
/**
 * My own class, which works with [[scala.Option]] and [[scalaz.Monad]].
 */
class MyClass(optionMonad: scalaz.Monad[Option])
```

Unfortunately when running `doc` command in Sbt, you will receive a warning and the link would not be created.

```
/path/to/MyClass.scala:3: Could not find any member to link for "scala.Option".
```

This plugin resolves the problem.

## Usage

### Step 1: Add the following lines in your `project/plugins.sbt`:

``` sbt
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "latest.release")
```

Note that sbt-api-mappings 1.x requires sbt 0.13.x, sbt-api-mappings 2.x requires sbt 1.x, and sbt-api-mappings 3.x is cross-built for both sbt 1.x and sbt 2.x.

### Step 2: Reload the Sbt configuration:

```
> reload
```

### Step 3: Generate your API documentation:

```
> doc
```

Now, open the API documentation in your browser, and you will find the links to the `scala.Option` and `scalaz.Monad`'s documentation in your `MyClass` page.
