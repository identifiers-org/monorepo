#!/usr/bin/env bash
# Produce paths for the rest API
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

url_registry_base='https://registry.api.identifiers.org/restApi/resources/'
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
