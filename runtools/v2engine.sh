#!/bin/bash

if [ $# != 1 ] || [ $1 == "--help" ]; then
  echo "Invalid cli syntax: V2Engine [filepath]"
else
  java -jar ~/bin/v2engine.jar $1
fi
