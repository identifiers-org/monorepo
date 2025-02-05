# This Makefile helps with some DevOps tasks related to the software lifecycle of this service

# These are meant to be run from the parent folder

# Environment
mirid_container_name = identifiersorg/cloud-hq-ws-mirid-controller
mirid_docker_compose_development_file = mirid-controller/docker-compose-development.yml
mirid_springboot_development_profile = development
mirid_tag_version = $(shell cat mirid-controller/VERSION)

mirid_sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${mirid_tag_version}'"
	@mvn versions:set -DnewVersion=${mirid_tag_version} -pl mirid-controller -DgenerateBackupPoms=false

mirid_development_env_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker-compose -f $(mirid_docker_compose_development_file) up -d

mirid_development_env_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker-compose -f $(mirid_docker_compose_development_file) down

mirid_app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Application"
	@mvn clean package -DskipTests -pl mirid-controller -am
	@cd mirid-controller; mkdir -p target/app/log; mkdir -p target/app/tmp
	@cp mirid-controller/target/mirid-controller.jar mirid-controller/target/app/service.jar

mirid_container_production_build: mirid_app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(mirid_container_name):$(mirid_tag_version)"
	@docker build -t $(mirid_container_name):$(mirid_tag_version) -t $(mirid_container_name):latest mirid-controller/

mirid_container_production_push: mirid_container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH] Production container $(mirid_container_name):$(mirid_tag_version)"
	@docker push $(mirid_container_name):$(mirid_tag_version)
	@docker push $(mirid_container_name):latest
