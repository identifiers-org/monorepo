#!/usr/bin/env bash
# Deploy HQ secrets
# This is a quick script to get the secrets
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

# Tools
JQ="$HOME/apps/bin/jq"

# Environment
BASEDIR=$(dirname "$0")
path_file_config=$1

# Import helpers
source $BASEDIR/logging.sh

loginfo "[CONFIG] Loading configuration from $path_file_config"
for secret in $( cat $path_file_config | $JQ -c ".[]" ); do
    secret_name=$(echo $secret | $JQ -r '.name')
    kubectl_command="kubectl create secret generic $secret_name"
    loginfo "[DEPLOYMENT] Deploying secret '$secret_name'"
    for secret_content_item in $( echo $secret | $JQ -c '.keyValuePayload[]' ); do
        for key in $( echo $secret_content_item | $JQ -c -r 'keys[]' ); do
            value=$(echo $secret_content_item | $JQ -c -r ".$key")
            loginfo "\tIncluding '$key=$value'"
            kubectl_command="$kubectl_command --from-literal=$key=$value"
        done
    done
    loginfo "[COMMAND] $kubectl_command"
    $kubectl_command
done