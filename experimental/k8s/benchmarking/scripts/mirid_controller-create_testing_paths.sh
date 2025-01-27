#!/usr/bin/env bash
# Produce paths for the rest API, this is something I need quick and dirty to test out something on the rest API
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

url_registry_base='https://mirid.api.identifiers.org'
file_output_paths='mirid_restapi_paths.txt'

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
for i in $( seq 1 2000 ); do
    test_url="${url_registry_base}/restApi/activeMirIds/$i"
    curl -i $test_url 2> /dev/null | grep HTTP | grep 200 > /dev/null
    if [ "`echo $?`" == "0" ]; then
        echo "/restApi/activeMirIds/$i" >> $file_output_paths
        loginfo "[ADD] $test_url"
    fi
done
loginfo "[SEARCH] Calculating URLs for namespaces"
for i in $( seq 700 2200 ); do
    test_url="${url_registry_base}/restApi/returnedMirIds/$i"
    curl -i ${url_registry_base}/restApi/returnedMirIds/$i 2> /dev/null | grep HTTP | grep 200 > /dev/null
    if [ "`echo $?`" == "0" ]; then
        echo "/restApi/returnedMirIds/$i" >> $file_output_paths
        loginfo "[ADD] $test_url"
    fi
done
loginfo "[DONE] URL calculation has now been completed."