# This Makefile helps on building the production container image

sparql_container_name = identifiersorg/sparql-identifiers
springboot_development_profile = development
sparql_tag_version = $(shell cat sparql/VERSION)

sparql_sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${sparql_tag_version}'"
	@mvn versions:set -DnewVersion=${sparql_tag_version} -pl sparql -DgenerateBackupPoms=false

sparql_container_production_build: sparql_app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(container_middleend_name):$(sparql_tag_version)"
	@docker build -t $(sparql_container_name):$(sparql_tag_version) -t $(sparql_container_name):latest sparql/

sparql_container_production_push: sparql_container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(sparql_container_name):$(sparql_tag_version)"
	@docker push $(sparql_container_name):$(sparql_tag_version)
	@docker push $(sparql_container_name):latest
