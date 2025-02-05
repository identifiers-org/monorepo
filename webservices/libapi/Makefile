# This Makefile helps con managing environments and deployment of this library

# These are meant to be run from the parent folder

lib_api_docker_compose_development_file = libapi/docker-compose-development.yml
lib_api_tag_version = $(shell cat libapi/VERSION)

libapi_sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${lib_api_tag_version}'"
	@mvn versions:set -DnewVersion=${lib_api_tag_version} -DgenerateBackupPoms=false -pl libapi

libapi_development_env_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker-compose -f $(lib_api_docker_compose_development_file) up -d
	@# TODO Clean this way of referencing the target name in future iterations

libapi_development_env_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker-compose -f $(lib_api_docker_compose_development_file) down
	@# TODO Clean this way of referencing the target name in future iterations

