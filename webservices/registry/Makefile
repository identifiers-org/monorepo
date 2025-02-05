# This Makefile helps on building the production container image

# These are meant to be run from the parent folder

# Environment
registry_container_name = identifiersorg/cloud-hq-ws-registry
registry_docker_compose_development_file = registry/docker-compose-development.yml
registry_springboot_development_profile = development
registry_tag_version = $(shell cat registry/VERSION)

registry_sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${registry_tag_version}'"
	@mvn versions:set -DnewVersion=${registry_tag_version} -pl registry

registry_development_env_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker compose -f $(registry_docker_compose_development_file) up -d

registry_development_env_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker compose -f $(registry_docker_compose_development_file) down

registry_app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Application"
	@mvn clean package -DskipTests -pl registry -am
	@mkdir -p registry/target/app/log
	@mkdir -p registry/target/app/tmp
	@cp registry/target/registry.jar registry/target/app/service.jar

registry_container_production_build: registry_app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(registry_container_name):$(registry_tag_version)"
	@docker build -t $(registry_container_name):$(registry_tag_version) -t $(registry_container_name):latest registry/

registry_container_production_push: registry_container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(registry_container_name):$(registry_tag_version)"
	@docker push $(registry_container_name):$(registry_tag_version)
	@docker push $(registry_container_name):latest
