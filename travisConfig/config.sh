# plantUML generation
plantumlDir="$baseDir/tools/plantuml.jar"
plantumlOutput="images"
plantumlCommand=$(java -jar $plantumlDir -o $plantumlOutput *.plantuml)