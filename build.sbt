enablePlugins(ScalaJSPlugin)

libraryDependencies += "org.scala-lang.modules" %%% "scala-parser-combinators" % "1.0.5"

scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
