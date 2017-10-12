#!/bin/bash
LOCATION1="./sageServer.jar"
LOCATION2="./target/sageServer.jar"
if [ -f "$LOCATION1" ]
then
    java -jar $LOCATION1
else
    java -jar $LOCATION2
fi
