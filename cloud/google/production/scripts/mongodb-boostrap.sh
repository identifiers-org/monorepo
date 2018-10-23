#!/usr/bin/env bash
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>
source tinylogger.bash

# Set the logging level
LOGGER_LVL=debug

# Environment configuration
MONGODB_BOOTSTRAP_FOLDER_TMP=${MONGODB_BOOTSTRAP_FOLDER_TMP:="tmp"}
# Replicas
MONGODB_BOOTSTRAP_N_REPLICAS=${MONGODB_BOOTSTRAP_N_REPLICAS:="3"}
# Destination Kubernetes cluster coordinates
MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME=${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME:=`kubectl config current-context`}
MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION=${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION:=`gcloud container clusters list --filter=${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME} --format=yaml | grep "zone:" | cut -f2 -d' '`}
# Some behavioral checks
FLAG_DUMP_ADMIN_CREDENTIALS=0
if [ -z ${MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME+x} ]; then
    FLAG_DUMP_ADMIN_CREDENTIALS=1
fi
# File for dumping admin credentials
MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS=${MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS:="${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME}-mongodb-admin-credentials.yml"}
# MongoDB admin credentials
MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME=${MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME:="mongadmin"}
MONGODB_BOOTSTRAP_MONGODB_ADMIN_PASSWORD=${MONGODB_BOOTSTRAP_MONGODB_ADMIN_PASSWORD:=`uuidgen`}
# MongoDB Kubernetes definition file
MONGODB_BOOTSTRAP_FILE_KUBERNETES_DEFINITION=${MONGODB_BOOTSTRAP_FILE_KUBERNETES_DEFINITION:="../kubernetes/services/mongodb.yml"}

# Helpers
function info() {
    tlog info "[DEVOPS] Replicas: ${MONGODB_BOOTSTRAP_N_REPLICAS}"
    tlog info "[DEVOPS] Kubernetes Cluster: ${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME}"
    tlog info "[DEVOPS] Kubernetes Region: ${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION}"
    if [ "$FLAG_DUMP_ADMIN_CREDENTIALS" == 1 ]; then
        tlog info "[DEVOPS] Admin Credentials file: ${MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS}"
    fi
    tlog info "[DEVOPS] MongoDB kubernetes definition at ${MONGODB_BOOTSTRAP_FILE_KUBERNETES_DEFINITION}"
}

# General Information before we start deploying MongoDB for the given configuration
tlog info "[--- MongoDB Backend Bootstrap ---]"
info
if [ "$FLAG_DUMP_ADMIN_CREDENTIALS" == 1 ]; then
    tlog info "Dumping Administrator credentials to $MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS"
    echo "username: $MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME" >> $MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS
    echo "password: $MONGODB_BOOTSTRAP_MONGODB_ADMIN_PASSWORD" >> $MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS
fi
