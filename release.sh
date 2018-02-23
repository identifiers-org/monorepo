#!/bin/bash
#
# This script is a helper in the release cycle of the containerized service
#
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

# Defaults
verb=nothing
message=""
version=$(cat VERSION)

if [ $# -lt 1 ]
then
  echo "usage: $(basename $0) patch|minor|major major.minor.patch"
  exit 1
fi

# Read verb
verb=$1

# Process release
if [ "${verb}" == "patch" ]
then
    echo "<===|DEVOPS|===> [RELEASE] PATCH"
    echo -e "\tCurrent version '${version}'"
    version=$(./increment_version.sh -p ${version})
    echo -e "\tNew version '${version}'"
fi
