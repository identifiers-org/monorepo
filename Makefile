# This Makefile helps on building the production container image
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

# Environment
container_name = identifiersorg/cloud-hq-web-frontend
docker_compose_development_file = docker-compose-development.yml
springboot_development_profile = development
tag_version = $(shell cat VERSION)
dev_site_root_folder = site

# Default target
all: deploy

release: deploy
	@echo "<===|DEVOPS|===> [RELEASE] New Software Release, and next development version prepared"

deploy: clean container_production_push
	@echo "<===|DEVOPS|===> [DEPLOY] Deploying service container version ${tag_version}"

development_env_up: development_env_backend_up
	@echo "<===|DEVOPS|===> [DEVELOPMENT] Launch development environment"
	@cd ${dev_site_root_folder}; docker run -it node npm start

development_env_backend_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker-compose -f $(docker_compose_development_file) up -d
	@# TODO Clean this way of referencing the target name in future iterations
	@rm -f development_env_backend_down
	@touch development_env_backend_up

development_env_backend_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker-compose -f $(docker_compose_development_file) down
	@# TODO Clean this way of referencing the target name in future iterations
	@rm -f development_env_backend_up
	@touch development_env_backend_down

development_run_tests: development_env_up
	@echo "<===|DEVOPS|===> [TESTS] Running Unit Tests"
	@mvn -Dspring.profiles.active=$(springboot_development_profile) clean test

app_structure: build css spa
	@echo "<===|DEVOPS|===> [PACKAGE] Building application structure"
	@cp -R site/* build/.

css:
	@echo "<===|DEVOPS|===> [PACKAGE] CSS"

spa:
	@echo "<===|DEVOPS|===> [PACKAGE] SPA"

container_production_build: app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(container_name):$(tag_version)"
	@docker build -t $(container_name):$(tag_version) -t $(container_name):latest .

container_production_push: container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(container_name):$(tag_version)"
	@docker push $(container_name):$(tag_version)
	@docker push $(container_name):latest

dev_container_build: clean container_production_build
	@echo "<===|DEVOPS|===> [DEV] Preparing local container"

# Folders
build:
	@echo "<===|DEVOPS|===> [FOLDER] Preparing 'build' folder"
	@mkdir build

clean:
	@echo "<===|DEVOPS|===> [CLEAN] Cleaning 'build'"
	@rm -rf build

.PHONY: all clean app_structure css spa container_production_build development_webapp_up container_production_push dev_container_build deploy release sync_project_version set_next_development_version
