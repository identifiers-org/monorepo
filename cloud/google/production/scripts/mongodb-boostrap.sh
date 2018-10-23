#!/usr/bin/env bash
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>
source tinylogger.bash

# Set the logging level
LOGGER_LVL=debug

# Environment configuration
# Replicas
HQ_MONGODB_BOOTSTRAP_N_REPLICAS=${HQ_MONGODB_BOOTSTRAP_N_REPLICAS:="3"}
# Destination Kubernetes cluster coordinates
HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME=${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME:=`kubectl config current-context`}
HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION=${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION:=`gcloud container clusters list --filter=${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME} --format=yaml | grep "zone:" | cut -f2 -d' '`}
# Some behavioral checks
FLAG_DUMP_ADMIN_CREDENTIALS=0
if [ -z ${HQ_MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME+x} ]; then
    FLAG_DUMP_ADMIN_CREDENTIALS=1
fi
# File for dumping admin credentials
HQ_MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS=${HQ_MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS:="${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME}-mongodb-admin-credentials.yml"}
# MongoDB admin credentials
HQ_MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME=${HQ_MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME:="mongadmin"}
HQ_MONGODB_BOOTSTRAP_MONGODB_ADMIN_PASSWORD=${HQ_MONGODB_BOOTSTRAP_MONGODB_ADMIN_PASSWORD:=`uuidgen`}
# MongoDB Kubernetes definition file
HQ_MONGODB_BOOTSTRAP_FILE_KUBERNETES_DEFINITION=${HQ_MONGODB_BOOTSTRAP_FILE_KUBERNETES_DEFINITION:="../kubernetes/services/mongodb.yml"}

# Helpers
function info() {
    tlog info "[DEVOPS] Replicas: ${HQ_MONGODB_BOOTSTRAP_N_REPLICAS}"
    tlog info "[DEVOPS] Kubernetes Cluster: ${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME}"
    tlog info "[DEVOPS] Kubernetes Region: ${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION}"
    if [ "$FLAG_DUMP_ADMIN_CREDENTIALS" == 1 ]; then
        tlog info "[DEVOPS] Admin Credentials file: ${HQ_MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS}"
    fi
    tlog info "[DEVOPS] MongoDB kubernetes definition at ${HQ_MONGODB_BOOTSTRAP_FILE_KUBERNETES_DEFINITION}"
}

# General Information before we start deploying MongoDB for the given configuration
tlog info "[--- MongoDB Backend Bootstrap ---]"
info
if [ "$FLAG_DUMP_ADMIN_CREDENTIALS" == 1 ]; then
    tlog info "Dumping user credentials to $HQ_MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS"
    echo "username: $HQ_MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME" >> $HQ_MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS
    echo "password: $HQ_MONGODB_BOOTSTRAP_MONGODB_ADMIN_PASSWORD" >> $HQ_MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS
fi
