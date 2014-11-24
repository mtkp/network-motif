#!/bin/bash -e

# usage
( test -z $2 ) &&
  echo "usage: run.sh data_file motif_size [--show-results]" &&
  echo "       (default is to not show results)" &&
  echo "  ex1: run.sh path/to/data 3" &&
  echo "  ex2: run.sh path/to/data 6 --show-results" &&
  exit 1

# execution variables
program="Driver"

# execution setup
data_file=$1
motif_size=$2
show_results=$3
cp_additions="build"

# execute
java -Xms1g -Xmx8g -cp build:. $program $data_file $motif_size $show_results