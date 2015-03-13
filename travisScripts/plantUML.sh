#!/bin/bash
# Project E-ICGD
# Contributors:
#		Pierre Jeanjean
#		Quentin Lacoste
#		Florian Ouddane
# 	Anselme Revuz

count=$(ls -1 $plantumlSources/*.$plantumlExtension 2>/dev/null | wc -l)

echo count

if [ $count > 0 ] ; then
	plantumlCommand
else
	echo "No \".$plantumlExtension\" file found"
fi
