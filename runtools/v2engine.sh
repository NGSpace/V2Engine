#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

if [ $# != 1 ] || [ $1 == "--help" ]; then
  echo "Invalid cli syntax: V2Engine [filepath]"
else
  java -jar "${SCRIPT_DIR}/v2engine.jar" $1
fi