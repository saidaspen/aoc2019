#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
PKG_NAME=${1,,}

java -cp $DIR se.saidaspen.aoc2019.$PKG_NAME.$1 $2
