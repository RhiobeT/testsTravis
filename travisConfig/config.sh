# documentation generation
sourceDir="$baseDir/srcdoc"
sourceName="_source.adoc"
stylesheetDir="$sourceDir/stylesheet"
stylesheetName="asciidoctor.css"
resultHtmlName="_result.html"
preResultName="_pre-result.html"
resultMediawikiName="_result.mediawiki"

function htmlGenerationCommand {
	asciidoctor -a toc=macro, data-uri, stylesheet=$stylesheetName, stylesdir=$stylesheetDir -o $resultName $sourceName
}

function mediawikiGenerationCommand1 {
	asciidoctor -a stylesheet=$stylesheetName, numbered!, stylesdir=$stylesheetDir -o $preResultName $sourceName
}
	
function mediawikiGenerationCommand2 {
	pandoc -f html -t mediawiki -o $resultMediawikiName $preResultName
}





# plantUML generation
plantumlDir="$baseDir/tools/plantuml.jar"
plantumlOutput="$baseDir/images"

function plantumlCommand {
	java -jar $plantumlDir -o $plantumlOutput *.plantuml)
}