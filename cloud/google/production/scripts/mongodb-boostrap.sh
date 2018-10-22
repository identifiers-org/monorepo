#!/usr/bin/env bash
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>
source tinylogger.bash

# Set the logging level
LOGGER_LVL=debug

# Environment configuration
HQ_MONGODB_BOOTSTRAP_N_REPLICAS=${HQ_MONGODB_BOOTSTRAP_N_REPLICAS:="3"}
KUBERNETES_CLUSTER_NAME=`kubectl config current-context`

# Helpers
function info() {
    tlog info "[DEVOPS] Replicas: ${HQ_MONGODB_BOOTSTRAP_N_REPLICAS}"
    tlog info "[DEVOPS] Kubernetes Cluster: ${KUBERNETES_CLUSTER_NAME}"
}

tlog info "[--- MongoDB Backend Bootstrap ---]"
info
