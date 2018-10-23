#!/usr/bin/env bash
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>
source tinylogger.bash

# Set the logging level
LOGGER_LVL=debug

# Environment configuration
HQ_MONGODB_BOOTSTRAP_N_REPLICAS=${HQ_MONGODB_BOOTSTRAP_N_REPLICAS:="3"}
HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME=${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME:=`kubectl config current-context`}
HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION=${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION:=`gcloud container clusters list --filter=${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME} --format=yaml | grep "zone:" | cut -f2 -d' '`}

# Helpers
function info() {
    tlog info "[DEVOPS] Replicas: ${HQ_MONGODB_BOOTSTRAP_N_REPLICAS}"
    tlog info "[DEVOPS] Kubernetes Cluster: ${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME}"
    tlog info "[DEVOPS] Kubernetes Region: ${HQ_MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION}"
}

tlog info "[--- MongoDB Backend Bootstrap ---]"
info
