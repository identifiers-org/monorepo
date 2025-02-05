# This Makefile helps on building the production container image

# These are meant to be run from the parent folder

resolver_container_name = identifiersorg/cloud-ws-resolver
resolver_docker_compose_development_file = resolver/docker-compose-development.yml
resolver_springboot_development_profile = development
resolver_tag_version = $(shell cat resolver/VERSION)

sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${resolver_tag_version}'"
	@mvn versions:set -DnewVersion=${resolver_tag_version} -DgenerateBackupPoms=false -pl resolver

resolver_development_env_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker compose -f $(resolver_docker_compose_development_file) up -d

resolver_development_env_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker compose -f $(resolver_docker_compose_development_file) down

resolver_app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Application"
	@mvn clean package -DskipTests -pl resolver -am
	@mkdir -p resolver/target/app/log; mkdir -p resolver/target/app/tmp
	@cp resolver/target/resolver.jar resolver/target/app/service.jar

resolver_container_production_build: resolver_app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(resolver_container_name):$(resolver_tag_version)"
	@docker build -t $(resolver_container_name):$(resolver_tag_version) -t $(resolver_container_name):latest resolver/

resolver_container_production_push: resolver_container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(resolver_container_name):$(resolver_tag_version)"
	@docker push $(resolver_container_name):$(resolver_tag_version)
	@docker push $(resolver_container_name):latest
