name := "Simple Project"

version := "1.0"

scalaVersion := "2.10.6"

//libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.0"

resolvers += "Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven"

//libraryDependencies += "datastax" % "spark-cassandra-connector" % "2.0.0-s_2.11"

libraryDependencies += "org.apache.kafka" % "kafka_2.10" % "0.10.2.0"

resolvers += Resolver.mavenLocal
