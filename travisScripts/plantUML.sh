#!/bin/bash

count=`ls -1 *.plantuml 2>/dev/null | wc -l`

if [ $count > 0 ] ; then
	java -jar $baseDir/tools/plantuml.jar -o images *.plantuml
else
	echo 'No ".plantuml" file found'
fi
