#!/bin/bash

count=$(ls -1 *.plantuml 2>/dev/null | wc -l)

if [ $count > 0 ] ; then
	plantumlCommand
else
	echo 'No ".plantuml" file found'
fi