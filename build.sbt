name := "storynames"

version := "1.0-SNAPSHOT"

scalaVersion := "2.9.2"

organization := "net.surguy.storynames"


// add a maven-style repository
resolvers += "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

externalPom()

// append several options to the list of options passed to the Java compiler
javacOptions ++= Seq("-source", "1.6", "-target", "1.6")


// fork a new JVM for 'run' and 'test:run'
fork := true

// fork a new JVM for 'test:run', but not 'run'
fork in Test := true

// add a JVM option to use when forking a JVM for 'run'
javaOptions += "-Xmx1024m"

// only show 10 lines of stack traces
traceLevel := 10

// Copy all managed dependencies to <build-root>/lib_managed/
//   This is essentially a project-local cache and is different
//   from the lib_managed/ in sbt 0.7.x.  There is only one
//   lib_managed/ in the build root (not per-project).
retrieveManaged := false

// Execute everything serially (including compilation and tests)
parallelExecution := false








