#!/bin/bash
echo "[DEVOPS] Configuring environment"
for configparam in $( egrep -o "ENVCONFIG[A-Z_]*" /home/site/index.html )
do
    echo -e "\t${configparam}"
    if [[ -z "${configparam}" ]]; then
        echo -e "\t\tNOT DEFINED"
    else
        value=$(eval "echo \$$configparam")
        sed -i "s/${configparam}/$value/g" /home/site/index.html
    fi
done
echo "[START] Starting HQ Registry Web Frontend"
nginx -g "daemon off;"
