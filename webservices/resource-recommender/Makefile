# This Makefile helps on building the production container image

# These are meant to be run from the parent folder

rr_container_name = identifiersorg/cloud-ws-resource-recommender
rr_docker_compose_development_file = resource-recommender/docker-compose-development.yml
rr_springboot_development_profile = development
rr_tag_version = $(shell cat resource-recommender/VERSION)

rr_sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${rr_tag_version}'"
	@mvn versions:set -DnewVersion=${rr_tag_version} -DgenerateBackupPoms=false -pl resource-recommender

rr_development_env_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker-compose -f $(rr_docker_compose_development_file) up -d

rr_development_env_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker-compose -f $(rr_docker_compose_development_file) down

rr_app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Application"
	@mvn clean package -DskipTests -pl resource-recommender -am
	@mkdir -p resource-recommender/target/app/log
	@mkdir -p resource-recommender/target/app/tmp
	@cp resource-recommender/target/resource-recommender.jar resource-recommender/target/app/service.jar

rr_container_production_build: rr_app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(rr_container_name):$(rr_tag_version)"
	@docker build -t $(rr_container_name):$(rr_tag_version) -t $(rr_container_name):latest resource-recommender/

rr_container_production_push: rr_container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(rr_container_name):$(rr_tag_version)"
	@docker push $(rr_container_name):$(rr_tag_version)
	@docker push $(rr_container_name):latest
