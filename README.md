# sbt-api-mappings

[![Build Status](https://travis-ci.org/ThoughtWorksInc/sbt-api-mappings.svg?branch=master)](https://travis-ci.org/ThoughtWorksInc/sbt-api-mappings)

**sbt-api-mappings** is a Sbt plugin that fills `apiMappings` for common Scala libraries.

## Motivation

Sometimes when you wrote ScalaDoc for your own classes, you may want to reference to documentation in some other libraries.

For example:

``` scala
/**
 * My own class, which works with [[scala.Option]] and [[scalaz.Monad]].
 */
class MyClass(optionMonad: scalaz.Monad[Option])
```

Unfortunately when use run `doc` command in Sbt, you will receive a warning and the link would not be created.

```
/path/to/MyClass.scala:3: Could not find any member to link for "scala.Option".
```

This plugin resolves the problem.

### Usage

#### Step 1: Add the following lines in your `project/plugins.sbt`:

``` sbt
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "latest.release")
```

#### Step 2: Reload the Sbt configuration:

```
> reload
```

#### Step 3: Generate your API documentation:

```
> doc
```

Now, open the API documentation in your browser, and you will find the links to the `scala.Option` and `scalaz.Monad`'s documentation in your `MyClass` page.
