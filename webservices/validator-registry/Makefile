# This Makefile helps on building the production container image, and with the release cycle

# These are meant to be run from the parent folder

validatorcontainer_name = identifiersorg/cloud-ws-link-checker
validatordocker_compose_development_file = validator-registry/docker-compose-development.yml
validatorspringboot_development_profile = development
validatortag_version = $(shell cat validator-registry/VERSION)

validatorsync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${validatortag_version}'"
	@mvn versions:set -DnewVersion=${validatortag_version} -DgenerateBackupPoms=false -pl link-checker

validatorapp_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Application"
	@mvn clean package -DskipTests -pl link-checker -am
	@mkdir -p validator-registry/target/app/log
	@mkdir -p validator-registry/target/app/tmp
	@cp validator-registry/target/link-checker.jar validator-registry/target/app/service.jar

validatorcontainer_production_build: validatorapp_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(validatorcontainer_name):$(validatortag_version)"
	@docker build -t $(validatorcontainer_name):$(validatortag_version) -t $(validatorcontainer_name):latest validator-registry/

validatorcontainer_production_push: validatorcontainer_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(validatorcontainer_name):$(validatortag_version)"
	@docker push $(validatorcontainer_name):$(validatortag_version)
	@docker push $(validatorcontainer_name):latest