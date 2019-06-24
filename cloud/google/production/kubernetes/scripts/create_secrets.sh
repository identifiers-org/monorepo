#!/usr/bin/env bash
# Deploy HQ secrets
# This is a quick script to get the secrets
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

# Environment
BASEDIR=$(dirname "$0")
path_file_config=$1

# Import helpers
source $BASEDIR/logging.sh

loginfo "[CONFIG] Loading configuration from $path_file_config"
loginfo "[DEPLOYMENT] Deploying secrets"