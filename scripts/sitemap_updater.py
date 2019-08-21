#!/usr/bin/env python
# coding: utf-8
# This scripts updates the sitemap.txt file
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

import os
import json
import time
import logging
import requests
from shutil import copyfile
from requests.exceptions import HTTPError

# Placeholder for environment variables

# Configure Logging
logFormatter = logging.Formatter("[SITEMAP_BUILDER]%(asctime)s [%(threadName)-12.12s] [%(levelname)-5.5s]  %(message)s")
rootLogger = logging.getLogger()
consoleHandler = logging.StreamHandler()
consoleHandler.setFormatter(logFormatter)
rootLogger.addHandler(consoleHandler)
rootLogger.setLevel(logging.DEBUG)

# Static
PLACEHOLDER_NAMESPACE_PREFIX = 'PLACEHOLDER_NAMESPACE_PREFIX'
SITEMAP_BUILDER_CONFIG_URL_REGISTRY_API = 'SITEMAP_BUILDER_CONFIG_URL_REGISTRY_API'
SITEMAP_BUILDER_CONFIG_URL_REGISTRY_SPA = 'SITEMAP_BUILDER_CONFIG_URL_REGISTRY_SPA'
SITEMAP_BUILDER_CONFIG_PATH_FILE_SITEMAP = 'SITEMAP_BUILDER_CONFIG_PATH_FILE_SITEMAP'
SITEMAP_BUILDER_CONFIG_SITEMAP_REBUILD_WAIT_SECONDS = 'SITEMAP_BUILDER_CONFIG_SITEMAP_REBUILD_WAIT_SECONDS'
SITEMAP_BUILDER_CONFIG_SITEMAP_REBUILD_WAIT_PACKET_SECONDS = 'SITEMAP_BUILDER_CONFIG_SITEMAP_REBUILD_WAIT_PACKET_SECONDS'

# Configuration
time_sitemap_rebuild_wait_seconds = os.getenv(SITEMAP_BUILDER_CONFIG_SITEMAP_REBUILD_WAIT_SECONDS, 86400)
time_sitemap_rebuild_wait_packet_seconds = os.getenv(SITEMAP_BUILDER_CONFIG_SITEMAP_REBUILD_WAIT_PACKET_SECONDS, 3600)
url_registry_api = os.getenv(SITEMAP_BUILDER_CONFIG_URL_REGISTRY_API, 'https://registry.api.identifiers.org')
url_registry_spa = os.getenv(SITEMAP_BUILDER_CONFIG_URL_REGISTRY_SPA, 'https://registry.identifiers.org')
url_registry_api_get_namespace_prefixes = "{}/registryInsightApi/getAllNamespacePrefixes".format(url_registry_api)
url_template_registry_namespace_landing_page = "{}/registry/{}#!".format(url_registry_spa, PLACEHOLDER_NAMESPACE_PREFIX)
path_file_sitemap = os.getenv(SITEMAP_BUILDER_CONFIG_PATH_FILE_SITEMAP, 'sitemap.txt')
path_file_sitemap_staging = "{}_staging".format(path_file_sitemap)

# Get the list of namespaces
try:
    while True:
        rootLogger.info("Getting Prefixes from '{}'".format(url_registry_api_get_namespace_prefixes))
        response = requests.get(url_registry_api_get_namespace_prefixes)
        rootLogger.info("Response Status Code: {}".format(response.status_code))
        response.raise_for_status()
        prefixes = json.loads(response.content)
        rootLogger.info("#{} prefixes obtained".format(len(prefixes)))
        rootLogger.info("Building staging sitemap at '{}'".format(path_file_sitemap_staging))
        with open(path_file_sitemap_staging, "w") as f:
            for prefix in sorted(prefixes):
                sitemap_url = url_template_registry_namespace_landing_page.replace(PLACEHOLDER_NAMESPACE_PREFIX, prefix)
                rootLogger.debug("SiteMap URL ---> '{}'".format(sitemap_url))
                f.write("{}\n".format(sitemap_url))
        with open(path_file_sitemap_staging, "r") as f:
            line_count = 0
            for line in f:
                line_count += 1
            rootLogger.info("SiteMap file has #{} lines, for #{} prefixes".format(line_count, len(prefixes)))
            if (line_count == len(prefixes)):
                rootLogger.info("Moving staging sitemap from '{}' to production sitemap at '{}'".format(path_file_sitemap_staging, path_file_sitemap))
                copyfile(path_file_sitemap_staging, path_file_sitemap)
            else:
                rootLogger.error("The number of URLs written to the Staging Sitemap at '{}' DOES NOT MATCH with the number of prefixes, #{}. Sitemap WILL NOT BE UPDATED".format(line_count, len(prefixes)))
        # Break down the sitemap wait into X seconds periods
        n_wait_packets = int(time_sitemap_rebuild_wait_seconds / time_sitemap_rebuild_wait_packet_seconds) + 1
        last_wait_packet_seconds = time_sitemap_rebuild_wait_seconds % time_sitemap_rebuild_wait_packet_seconds
        rootLogger.info("NEXT SITEMAP UPDATE IN {}s".format(time_sitemap_rebuild_wait_seconds))
        rootLogger.info("#{} Waiting loops, final wait time {}s".format(n_wait_packets, last_wait_packet_seconds))
        while n_wait_packets != 0:
            if ((n_wait_packets - 1) == 0):
                rootLogger.info("NEXT SITEMAP UPDATE IN {}s".format(last_wait_packet_seconds))
                time.sleep(last_wait_packet_seconds)
            else:
                next_update_in_seconds = ((n_wait_packets - 1) * time_sitemap_rebuild_wait_packet_seconds) + last_wait_packet_seconds
                rootLogger.info("NEXT SITEMAP UPDATE IN {}s".format(next_update_in_seconds))
                time.sleep(time_sitemap_rebuild_wait_packet_seconds)
            n_wait_packets -= 1
except Exception as e:
    rootLogger.error("Error '{}'".format(e))