#!/bin/bash
cat src/main/java/org/identifiers/cloud/hq/ws/registry/configuration/AuthSecurityConfiguration.java | egrep -o ".contains\('.+'\)\)" | egrep -o "\('.+'\)" | sed "s/('//g" | sed "s/')//g"
