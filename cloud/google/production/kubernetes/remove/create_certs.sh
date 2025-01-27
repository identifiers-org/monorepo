#!/bin/bash
gcloud beta compute ssl-certificates create cert-iorg-hq-production-web --domains registry.identifiers.org
gcloud beta compute ssl-certificates create cert-iorg-hq-production-accountsweb --domains accounts.identifiers.org
gcloud beta compute ssl-certificates create cert-iorg-hq-production-api-registry --domains registry.api.identifiers.org
gcloud beta compute ssl-certificates create cert-iorg-hq-production-api-mirid --domains mirid.api.identifiers.org
