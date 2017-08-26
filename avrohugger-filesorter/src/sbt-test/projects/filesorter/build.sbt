name := "filesorter-test"

sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue
