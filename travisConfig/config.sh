# documentation generation
sourceDir="$baseDir/srcdoc"
sourceName="_source.adoc"
stylesheetDir="$sourceDir/stylesheet"
stylesheetName="asciidoctor.css"
resultName="_result.html"
htmlGenerationCommand=$(asciidoctor -a toc=macro -a data-uri -a stylesheet=$stylesheetName -a stylesdir=$stylesheetDir -o $resultName $sourceName)

# plantUML generation
plantumlDir="$baseDir/tools/plantuml.jar"
plantumlOutput="$baseDir/images"
plantumlCommand=$(java -jar $plantumlDir -o $plantumlOutput *.plantuml)