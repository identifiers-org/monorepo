# This Makefile helps on building the production container image, and with the release cycle
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>	#
# Some "facts"
container_name = identifiersorg/cloud-ws-link-checker
docker_compose_development_file = docker-compose-development.yml
springboot_development_profile = development
tag_version = $(shell cat VERSION)

# Default target
all: deploy

release: deploy set_next_development_version
	@echo "<===|DEVOPS|===> [RELEASE] New Software Release, and next development version prepared"
	@git add pom.xml
	@git commit -am "Next project development version prepared"
	@git push

sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${tag_version}'"
	@mvn versions:set -DnewVersion=${tag_version}

set_next_development_version:
	@echo "<===|DEVOPS|===> [SYNC] Setting the new development version, current ${tag_version}"
	@mvn versions:set -DnewVersion=$(shell ./increment_version.sh -p ${tag_version})-SNAPSHOT
