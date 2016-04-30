#!/bin/bash

## Author: Glenn Jackman
## Modified by: SkaceKachna
## Link: http://stackoverflow.com/a/7335524/2205567

if type -p java > /dev/null; then
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    _java="$JAVA_HOME/bin/java"
else
    echo "Failed to find java executable. Please install JRE."
	exit 1
fi

$_java -jar ./minicloud.jar "$@"