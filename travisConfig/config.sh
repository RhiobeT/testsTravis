# documentation generation
sourceDir="$baseDir/srcdoc"
imagesDir="$sourceDir/images"
sourceName="_source.adoc"
stylesheetDir="$sourceDir/stylesheet"
stylesheetName="asciidoctor.css"
resultHtmlName="_result.html"
preResultName="_pre-result.html"
resultMediawikiName="_result.mediawiki"

function htmlGenerationCommand {
	asciidoctor -a toc=macro,data-uri,stylesheet=$stylesheetName,stylesdir=$stylesheetDir -o $resultHtmlName $sourceName
}

function mediawikiGenerationCommand1 {
	asciidoctor -a stylesheet=$stylesheetName,numbered!,stylesdir=$stylesheetDir -o $preResultName $sourceName
}
	
function mediawikiGenerationCommand2 {
	pandoc -f html -t mediawiki -o $resultMediawikiName $preResultName
}





# plantUML generation
plantumlDir="$baseDir/tools/plantuml.jar"
plantumlOutput="$imagesDir"

function plantumlCommand {
	java -jar $plantumlDir -o $plantumlOutput *.plantuml
}





# tests
testsSourceName="Test.java"
testsClassName="Test"
testsDir="$baseDir/testsdoc"

function compilationTests {
	javac -d $sourceDir $testsSourceName
}

function lancementTests {
	java $testsClassName
}





# push to GitHub
repository="https://${GH_TOKEN}:@github.com/RhiobeT/testsTravis.git"
branch="doc_release"
cloneDir="$baseDir/_doc_release"
successDir="$baseDir/doc_release"
failureDir="$baseDir/doc_failed"
