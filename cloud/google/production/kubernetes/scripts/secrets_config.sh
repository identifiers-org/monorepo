#!/usr/bin/env bash
# Logging module
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

# Helpers
function log() {
    header="$1"
    shift
    echo -e "[$header] $@"
}

function loginfo() {
    log INFO $@
}