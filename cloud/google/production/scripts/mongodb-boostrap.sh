#!/usr/bin/env bash
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>
source tinylogger.bash

# Set the logging level
LOGGER_LVL=debug

# Environment configuration
HQ_MONGODB_BOOTSTRAP_N_REPLICAS=${HQ_MONGODB_BOOTSTRAP_N_REPLICAS:="3"}

# Helpers
function info() {
    tlog info "[DEPLOYMENT][INFO] Replicas: ${HQ_MONGODB_BOOTSTRAP_N_REPLICAS}"
}

tlog info "[--- MongoDB Backend Bootstrap ---]"
info
