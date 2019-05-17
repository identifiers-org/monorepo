# This Makefile helps on building the production container image
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

# Environment
container_middleend_name = identifiersorg/cloud-satellite-web-middleend
container_middleend_dockerfile = Dockerfile
container_spa_name = identifiersorg/cloud-satellite-web-spa
container_spa_dockerfile = Dockerfile.spa
docker_compose_development_file = docker-compose-development.yml
springboot_development_profile = development
tag_version = $(shell cat VERSION)
dev_site_root_folder = site
folder_site_dist = site/dist

# Default target
all: deploy

release: deploy set_next_development_version
	@echo "<===|DEVOPS|===> [RELEASE] New Software Release, and next development version prepared"
	@git add pom.xml
	@git commit -am "Next project development version prepared"
	@git push

set_next_development_version:
	@echo "<===|DEVOPS|===> [SYNC] Setting the new development version, current ${tag_version}"
	@mvn versions:set -DnewVersion=$(shell ./increment_version.sh -p ${tag_version})-SNAPSHOT

sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${tag_version}'"
	@docker run -v $(shell pwd)/${dev_site_root_folder}:/home/site node /bin/bash -c "npm --prefix /home/site version ${tag_version}"
	@mvn versions:set -DnewVersion=${tag_version}

deploy: clean container_production_push
	@echo "<===|DEVOPS|===> [DEPLOY] Deploying service container version ${tag_version}"

force_npm_reinstall:
	@echo "<===|DEVOPS|===> [CLEAN] Deleting npm updated flag"
	@rm npm_install

npm_install:
	@echo "<===|DEVOPS|===> [DEVELOPMENT] Installing npm modules"
	@docker run --user node --network=hqwebnet -p 8192:8192 -v $(shell pwd)/${dev_site_root_folder}:/home/site -it node /bin/bash -c "npm --prefix /home/site install; npm rebuild"
	@touch npm_install

development_env_up: development_env_backend_up npm_install
	@echo "<===|DEVOPS|===> [DEVELOPMENT] Launch development environment"
	@docker run --user node --network=hqwebnet -p 8182:8182 -v $(shell pwd)/${dev_site_root_folder}:/home/site -it node /bin/bash -c "npm --prefix /home/site start"

development_env_down: development_env_backend_down

development_env_backend_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing backend UP"
	@docker-compose -f $(docker_compose_development_file) up -d
	@# TODO Clean this way of referencing the target name in future iterations
	@rm -f development_env_backend_down
	@touch development_env_backend_up

development_env_backend_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing backend DOWN"
	@docker-compose -f $(docker_compose_development_file) down
	@# TODO Clean this way of referencing the target name in future iterations
	@rm -f development_env_backend_up
	@touch development_env_backend_down

development_run_tests: development_env_up
	@echo "<===|DEVOPS|===> [TESTS] Running Unit Tests"
	@mvn -Dspring.profiles.active=$(springboot_development_profile) clean test

app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Building application structure"
	@mvn clean > /dev/null
	@mvn package -DskipTests
	@mkdir -p target/app/log
	@mkdir -p target/app/tmp
	@cp target/satellite-webspa-$(shell mvn help:evaluate -Dexpression=project.version | grep -v '^\[').jar target/app/service.jar
	@docker run -v $(shell pwd)/${dev_site_root_folder}:/home/site node /bin/bash -c "npm --prefix /home/site install; npm --prefix /home/site run build"

container_production_build: app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(container_middleend_name):$(tag_version)"
	@docker build -t $(container_middleend_name):$(tag_version) -t $(container_middleend_name):latest -f $(container_middleend_dockerfile)
	@echo "<===|DEVOPS|===> [BUILD] Production container $(container_spa_name):$(tag_version)"
	@docker build -t $(container_spa_name):$(tag_version) -t $(container_spa_name):latest -f $(container_spa_dockerfile)

container_production_push: container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(container_middleend_name):$(tag_version)"
	@docker push $(container_middleend_name):$(tag_version)
	@docker push $(container_middleend_name):latest
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(container_spa_name):$(tag_version)"
	@docker push $(container_spa_name):$(tag_version)
	@docker push $(container_spa_name):latest

dev_container_build: clean container_production_build
	@echo "<===|DEVOPS|===> [DEV] Preparing local container"

clean:
	@echo "<===|DEVOPS|===> [CLEAN] Housekeeping"
	@rm -rf ${folder_site_dist}
	@rm -rf ${dev_site_root_folder}/node_modules

.PHONY: all clean app_structure container_production_build development_env_up development_env_down container_production_push dev_container_build deploy release sync_project_version set_next_development_version force_npm_install
