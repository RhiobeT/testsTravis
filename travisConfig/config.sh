#!/bin/bash
# Project E-ICGD
# Contributors:
#		Pierre Jeanjean
#		Quentin Lacoste
#		Florian Ouddane
# 	Anselme Revuz

# essentiel
repository="https://${GH_TOKEN}:@github.com/RhiobeT/testsTravis.git"





# documentation generation
sourceDir="$baseDir/srcdoc"
iconsDir="$sourceDir/icons"
imagesDir="$sourceDir/images"
sourceName="_source.adoc"
stylesheetDir="./stylesheet"
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
plantumlExtension=".plantuml"
plantumlSources="$sourceDir"
plantumlOutput="$imagesDir"

function plantumlCommand {
	java -jar $plantumlDir -o $plantumlOutput $plantumlSources/*.$plantumlExtension
}





# tests
testsSourceName="Tests.java"
testsClassName="Tests"
testsDir="$baseDir/testsdoc"

function compilationTests {
	javac -d $sourceDir $testsDir/$testsSourceName
}

function lancementTests {
	java $testsClassName
}





# push to GitHub
branch="doc_release"
cloneDir="$baseDir/_doc_release"
successDir="$cloneDir/doc_release"
failureDir="$cloneDir/doc_failed"

toCommit=(
	"-R $imagesDir"
	"-R $iconsDir"
	"-R $stylesheetDir"
	"$sourceDir/*.html"
	"$sourceDir/*.mediawiki"
)

function copyToCommit {
	for element in "${toCommit[@]}" ; do
		cp --target-directory=$commitDir $element
	done
}