# This Makefile helps on building the production container image
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

# Environment
container_name = identifiersorg/cloud-hq-ws-registry
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

deploy: clean container_production_push
	@echo "<===|DEVOPS|===> [DEPLOY] Deploying service container version ${tag_version}"

development_env_up: tmp
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker compose -f $(docker_compose_development_file) up -d

development_env_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker compose -f $(docker_compose_development_file) down

development_run_tests: development_env_up
	@echo "<===|DEVOPS|===> [TESTS] Running Unit Tests"
	@mvn -Dspring.profiles.active=$(springboot_development_profile) clean test

app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Application"
	@mvn clean > /dev/null
	@mvn package -DskipTests
	@mkdir -p target/app/log
	@mkdir -p target/app/tmp
	@cp target/registry-$(shell mvn help:evaluate -Dexpression=project.version | grep -v '^\[').jar target/app/service.jar

container_production_build: app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(container_name):$(tag_version)"
	@docker build -t $(container_name):$(tag_version) -t $(container_name):latest .

container_dev_build: app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(container_name):$(tag_version)"
	@docker build -t $(container_name):dev -t $(container_name):latest .

container_production_push: container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(container_name):$(tag_version)"
	@docker push $(container_name):$(tag_version)
	@docker push $(container_name):latest

dev_container_build: clean container_production_build
	@echo "<===|DEVOPS|===> [DEV] Preparing local container"

# Folders
tmp:
	@echo "<===|DEVOPS|===> [FOLDER] Creating temporary folders"
	@mkdir -p tmp/fakesmtp

clean_tmp:
	@echo "<===|DEVOPS|===> [HOUSEKEEPING] Cleaning temporary folders"
	@rm -rf tmp

clean: clean_tmp
	@echo "<===|DEVOPS|===> [CLEAN] Cleaning the space"
	@mvn clean > /dev/null
	@mvn versions:commit

.PHONY: all clean app_structure container_production_build container_production_push dev_container_build deploy release sync_project_version set_next_development_version clean_tmp
