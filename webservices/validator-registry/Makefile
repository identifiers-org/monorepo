# This Makefile helps on building the production container image, and with the release cycle

# These are meant to be run from the parent folder

validator_container_name = identifiersorg/cloud-registry-validator
validator_docker_compose_development_file = validator-registry/docker-compose-development.yml
validator_springboot_development_profile = development
validator_tag_version = $(shell cat validator-registry/VERSION)

validator_sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${validator_tag_version}'"
	@mvn versions:set -DnewVersion=${validator_tag_version} -DgenerateBackupPoms=false -pl validator-registry

validator_app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Application"
	@mvn clean package -DskipTests -pl validator-registry -am
	@mkdir -p validator-registry/target/app/log
	@mkdir -p validator-registry/target/app/tmp
	@cp validator-registry/target/validator-registry.jar validator-registry/target/app/service.jar

validator_container_production_build: validator_app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(validator_container_name):$(validator_tag_version)"
	@docker build -t $(validator_container_name):$(validator_tag_version) -t $(validator_container_name):latest validator-registry/

validator_container_production_push: validator_container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(validator_container_name):$(validator_tag_version)"
	@docker push $(validator_container_name):$(validator_tag_version)
	@docker push $(validator_container_name):latest