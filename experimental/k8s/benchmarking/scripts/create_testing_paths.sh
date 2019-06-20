#!/usr/bin/env bash
# Produce paths for the rest API, this is something I need quick and dirty to test out something on the rest API
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

url_registry_base='https://registry.api.identifiers.org'
file_output_paths='registry_restapi_paths.txt'

# Helpers
function log() {
    header="$1"
    shift
    echo -e "[$header] $@"
}

function loginfo() {
    log INFO $@
}

# Clean
loginfo "[HOSEKEEPING] Empty the output file at '$file_output_paths'"
> $file_output_paths
loginfo "[SEARCH] Calculating URLs for resources"
for i in $( seq 1 1000 ); do
    curl -i ${url_registry_base}/restApi/resources/$i | grep HTTP | grep 200
    if [ "`echo $?`" == "0" ]; then
        echo "/restApi/resources/$i" >> $file_output_paths
    fi
done
loginfo "[SEARCH] Calculating URLs for namespaces"
for i in $( seq 1 1000 ); do
    curl -i ${url_registry_base}/restApi/namespaces/$i | grep HTTP | grep 200
    if [ "`echo $?`" == "0" ]; then
        echo "/restApi/namespaces/$i" >> $file_output_paths
    fi
done
loginfo "[DONE] URL calculation has now been completed."