#!/usr/bin/env bash
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>
BASEDIR=$(dirname "$0")
source "${BASEDIR}"/tinylogger.bash

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
MONGODB_BOOTSTRAP_FILE_TEMPLATE_KUBERNETES_DEFINITION=${MONGODB_BOOTSTRAP_FILE_TEMPLATE_KUBERNETES_DEFINITION:="kubernetes/services/mongodb_template.yml"}
MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_CLASS=${MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_CLASS:="kubernetes/storage/gce-ssd-storageclass.yml"}
MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_VOLUME_TEMPLATE=${MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_VOLUME_TEMPLATE:="kubernetes/storage/mongodb_volume_template.yml"}
MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_CLASS_NAME=${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_CLASS_NAME:=`yq -r .metadata.name ${MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_CLASS}`}
MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_TYPE=${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_TYPE:=`yq -r .parameters.type ${MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_CLASS}`}
MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_VOLUME_SIZE=${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_VOLUME_SIZE:=100Gi}
MONGODB_BOOTSTRAP_SECRET_NAME_MONGODB_AUTH="${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME}-mongodb"
MONGODB_BOOTSTRAP_SECRET_KEYFILE_MONGODB_AUTH="${MONGODB_BOOTSTRAP_FOLDER_TMP}/${MONGODB_BOOTSTRAP_SECRET_NAME_MONGODB_AUTH}.keyfile"

# Helpers
function info() {
    tlog info "[DEVOPS] Temporary Folder: ${MONGODB_BOOTSTRAP_FOLDER_TMP}"
    tlog info "[DEVOPS] Replicas: ${MONGODB_BOOTSTRAP_N_REPLICAS}"
    tlog info "[DEVOPS] Kubernetes Cluster: ${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME}"
    tlog info "[DEVOPS] Kubernetes Region: ${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION}"
    if [ "$FLAG_DUMP_ADMIN_CREDENTIALS" == 1 ]; then
        tlog info "[DEVOPS] Admin Credentials file: ${MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS}"
    fi
    tlog info "[DEVOPS] MongoDB kubernetes definition at ${MONGODB_BOOTSTRAP_FILE_TEMPLATE_KUBERNETES_DEFINITION}"
    tlog info "[DEVOPS] Kubernetes Storage Class Definition at ${MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_CLASS}"
    tlog info "[DEVOPS] Kubernetes Storage Class name '${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_CLASS_NAME}'"
    tlog info "[DEVOPS] Kubernetes Storage Type '${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_TYPE}'"
    tlog info "[DEVOPS] Kubernetes MongoDB Storage Volumes Template at ${MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_VOLUME_TEMPLATE}"
    tlog info "[DEVOPS] Kubernetes MongoDB Storage Volumes capacity, ${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_VOLUME_SIZE}"
}

# Steps
function dump_admin_credentials() {
    if [ "$FLAG_DUMP_ADMIN_CREDENTIALS" == 1 ]; then
        tlog info "[DEVOPS] Dumping Administrator credentials to $MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS"
        echo "username: $MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME" > $MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS
        echo "password: $MONGODB_BOOTSTRAP_MONGODB_ADMIN_PASSWORD" >> $MONGODB_BOOTSTRAP_FILE_MONGODB_ADMIN_CREDENTIALS
    fi
}

function setup_storage_class() {
    tlog info "[${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME}] Setting up Storage class '${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_CLASS_NAME}'"
    kubectl apply -f "${MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_CLASS}"
    tlog info "------------------------------------------------------------------------------------------------------"
    kubectl get all
    tlog info "------------------------------------------------------------------------------------------------------"
}

function create_persistent_disks() {
    VOLUME_NAME_PREFIX='mongodb-data-volume'
    zones=( a b c )
    for i in $(seq 1 $MONGODB_BOOTSTRAP_N_REPLICAS); do
        zone_idx=`echo "$i % 3" | bc`
        DISK_ZONE="${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_REGION}-${zones[$zone_idx]}"
        DISK_NAME="${MONGODB_BOOTSTRAP_KUBERNETES_CLUSTER_NAME}-mongodb-disk-$i"
        tlog info "[CLOUD] Creating Persistent Disk #$i (${DISK_ZONE}) (${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_VOLUME_SIZE}) - $DISK_NAME"
        gcloud compute disks create --size ${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_VOLUME_SIZE} --type ${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_TYPE} ${DISK_NAME} --zone=${DISK_ZONE}
        KUBERNETES_DATA_VOLUME_FILE="${MONGODB_BOOTSTRAP_FOLDER_TMP}/${VOLUME_NAME_PREFIX}-$i.yml"
        KUBERNETES_DATA_VOLUME_NAME="${VOLUME_NAME_PREFIX}-$i"
        tlog info "[DEVOPS] Preparing Kubernetes Volume ${KUBERNETES_DATA_VOLUME_NAME}"
        cp "${MONGODB_BOOTSTRAP_FILE_KUBERNETES_STORAGE_VOLUME_TEMPLATE}" "${KUBERNETES_DATA_VOLUME_FILE}"
        tlog debug "[DEVOPS] Set volume name to ${KUBERNETES_DATA_VOLUME_NAME}"
        sed -i 's/METADATA_NAME/'"${KUBERNETES_DATA_VOLUME_NAME}"'/g' ${KUBERNETES_DATA_VOLUME_FILE}
        tlog debug "[DEVOPS] Set the Capacity to ${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_VOLUME_SIZE}"
        sed -i 's/SPEC_CAPACITY_STORAGE/'"${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_VOLUME_SIZE}"'/g' ${KUBERNETES_DATA_VOLUME_FILE}
        tlog debug "[DEVOPS] Set the Storage Class to ${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_CLASS_NAME}"
        sed -i 's/SPEC_STORAGE_CLASS_NAME/'"${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_CLASS_NAME}"'/g' ${KUBERNETES_DATA_VOLUME_FILE}
        tlog debug "[DEVOPS] Set the Persistent Disk Name to ${DISK_NAME}"
        sed -i 's/SPEC_GCE_PERSISTENT_DISK_PD_NAME/'"${DISK_NAME}"'/g' ${KUBERNETES_DATA_VOLUME_FILE}
        tlog info "[CLOUD] Create Kubernetes Volume ${KUBERNETES_DATA_VOLUME_NAME}"
        kubectl apply -f ${KUBERNETES_DATA_VOLUME_FILE}
    done
    tlog info "------------------------------------------------------------------------------------------------------"
    # TODO - Show only disks in the current region
    gcloud compute disks list
    tlog info "------------------------------------------------------------------------------------------------------"
    kubectl get persistentVolumes
    tlog info "------------------------------------------------------------------------------------------------------"
}

function create_secrets_for_mongodb_cluster() {
    tlog info "[CLOUD] Creating Auth Secret for MongoDB cluster authentication"
    openssl rand -base64 741 > "${MONGODB_BOOTSTRAP_SECRET_KEYFILE_MONGODB_AUTH}"
    current_folder=`pwd`
    cd `dirname ${MONGODB_BOOTSTRAP_SECRET_KEYFILE_MONGODB_AUTH}`
    KEY_FILE=`basename ${MONGODB_BOOTSTRAP_SECRET_KEYFILE_MONGODB_AUTH}`
    kubectl create secret generic ${MONGODB_BOOTSTRAP_SECRET_NAME_MONGODB_AUTH} --from-file="${KEY_FILE}"
    cd $current_folder
    tlog info "------------------------------------------------------------------------------------------------------"
    kubectl get secrets
    tlog info "------------------------------------------------------------------------------------------------------"
}

function launch_stateful_set() {
    FILE_MONGODB_KUBERNETES_DEFINITION="${MONGODB_BOOTSTRAP_FOLDER_TMP}/mongodb.yml"
    cp "${MONGODB_BOOTSTRAP_FILE_TEMPLATE_KUBERNETES_DEFINITION}" "${FILE_MONGODB_KUBERNETES_DEFINITION}"
    tlog info "[DEVOPS] Preapre MongoDB Kubernetes definition at ${FILE_MONGODB_KUBERNETES_DEFINITION}"
    tlog debug "[DEVOPS] Set replicas to #${MONGODB_BOOTSTRAP_N_REPLICAS}"
    sed -i 's/PLACEHOLDER_MONGODB_REPLICAS/'"${MONGODB_BOOTSTRAP_N_REPLICAS}"'/g' ${FILE_MONGODB_KUBERNETES_DEFINITION}
    tlog debug "[DEVOPS] Set the authentication keyfile"
    sed -i 's/AUTH_KEY_FILE/'"`basename ${MONGODB_BOOTSTRAP_SECRET_KEYFILE_MONGODB_AUTH}`"'/g' ${FILE_MONGODB_KUBERNETES_DEFINITION}
    tlog debug "[DEVOPS] Set the secrets name for MongoDB Authentication to ${MONGODB_BOOTSTRAP_SECRET_NAME_MONGODB_AUTH}"
    sed -i 's/MONGODB_AUTH_SECRET_NAME/'"${MONGODB_BOOTSTRAP_SECRET_NAME_MONGODB_AUTH}"'/g' ${FILE_MONGODB_KUBERNETES_DEFINITION}
    tlog debug "[DEVOPS] Set the Storage class to ${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_CLASS_NAME}"
    sed -i 's/STORAGE_CLASS/'"${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_CLASS_NAME}"'/g' ${FILE_MONGODB_KUBERNETES_DEFINITION}
    tlog debug "[DEVOPS] Set the Storage size to ${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_VOLUME_SIZE}"
    sed -i 's/STORAGE_SIZE/'"${MONGODB_BOOTSTRAP_KUBERNETES_STORAGE_VOLUME_SIZE}"'/g' ${FILE_MONGODB_KUBERNETES_DEFINITION}
    tlog info "[CLOUD] Launch MongoDB Stateful Set"
    #kubectl apply -f ${FILE_MONGODB_KUBERNETES_DEFINITION}
    tlog info "[CLOUD] Wait for the StatefulSet to be ready"
    while [ "`kubectl get statefulset/mongod -o yaml | yq .status.currentReplicas`" != "${MONGODB_BOOTSTRAP_N_REPLICAS}" ]; do
        tlog info "[CLOUD] Only #`kubectl get statefulset/mongod -o yaml | yq .status.currentReplicas` out of #${MONGODB_BOOTSTRAP_N_REPLICAS} are up, waiting..."
        sleep 3
    done
    tlog info "------------------------------------------------------------------------------------------------------"
    kubectl get all
    tlog info "------------------------------------------------------------------------------------------------------"
}

function init_mongodb_cluster() {
    tlog info "[DEVOPS] Preparing to initialize the MongoDB cluster"
    MONGODB_CLUSTER_DOMAIN=`kubectl exec -it mongod-0 -- hostname -f | sed 's/mongod-0.//g'`
    FILE_INIT_COMMAND="${MONGODB_BOOTSTRAP_FOLDER_TMP}/cluster_init.command"
    N_MINUS_ONE_REPLICAS=`echo "${MONGODB_BOOTSTRAP_N_REPLICAS} - 1" | bc`
    echo 'rs.initiate({_id: "MainRepSet", version: 1, members: [' > ${FILE_INIT_COMMAND}
    for i in $(seq 0 ${N_MINUS_ONE_REPLICAS}); do
        echo -ne "\t{_id: $i, host:\"mongod-$i.${MONGODB_CLUSTER_DOMAIN}:27017\"}" >> ${FILE_INIT_COMMAND}
        if [ "${i}" != "${N_MINUS_ONE_REPLICAS}"]; then
            echo "," >> ${FILE_INIT_COMMAND}
        fi
    done
    echo "]});" >> ${FILE_INIT_COMMAND}
    tlog info "[DEVOPS] Initialize MongoDB Cluster"
    cat ${FILE_INIT_COMMAND} | kubectl exec -it mongod-0 -- mongo
    # Wait until replica status is Ok.
    tlog info "[DEVOPS] Waiting for the Replica Set to complete Initialization"
    #rs_ok=`kubectl exec -it mongod-0 -- mongo -u iorgmainadmin -p 5A5C09E2-4E4E-422F-88C7-1E8D722DA209 -eval 'rs.status().ok' --quiet admin 2> /dev/null`
    while [ "`kubectl exec -it mongod-0 -- mongo -eval 'rs.status().ok' --quiet 2> /dev/null | tr -d '\r'`" != "1" ]; do
        tlog debug "[DEVOPS] Not ready yet"
        sleep 3
        #rs_ok=`kubectl exec -it mongod-0 -- mongo -u iorgmainadmin -p 5A5C09E2-4E4E-422F-88C7-1E8D722DA209 -eval 'rs.status().ok' --quiet admin 2> /dev/null | tr -d '\r'`
        #tlog debug "Status: --- '$rs_ok' ---"
    done
}

function setup_admin_user() {
    tlog info "[DEVOPS] Configuring general 'admin' user"
    echo "db.getSiblingDB(\"admin\").createUser({user : \"${MONGODB_BOOTSTRAP_MONGODB_ADMIN_USERNAME}\", pwd : \"${MONGODB_BOOTSTRAP_MONGODB_ADMIN_PASSWORD}\", roles: [{role: \"root\", db: \"admin\"}]});" | kubectl exec -it mongod-0 -- mongo
}

# --- START ---
tlog info "[ [START]--- MongoDB Backend Bootstrap ---[START] ]"
# Print out a description of what's gonna happen
info
# Dump admin credentials
dump_admin_credentials
# TODO - Setup the Storage Class
#setup_storage_class
# TODO - Create Persistent Disks
#create_persistent_disks
# TODO - Create Secrets for MondoDB communication
#create_secrets_for_mongodb_cluster
# TODO - Launch StatefulSet
launch_stateful_set
# TODO - Init the MongoDB cluster
#init_mongodb_cluster
# TODO - Setup the admin user
#setup_admin_user
