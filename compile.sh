#!/bin/bash -e

# usage: 
#  $ compile.sh

# build program
echo "Building program..." 1>&2
if [ ! -d build ] ; then mkdir build; fi
rm -f build/*.class
find ./src -name "*.java" > src.java.list
javac $1 -cp . -d build @src.java.list

# clean up list files
rm -f *.java.list
