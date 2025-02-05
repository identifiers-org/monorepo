# This Makefile helps on building the production container image, and with the release cycle

# These are meant to be run from the parent folder

link_checker_container_name = identifiersorg/cloud-ws-link-checker
link_checker_docker_compose_development_file = link-checker/docker-compose-development.yml
link_checker_springboot_development_profile = development
link_checker_tag_version = $(shell cat link-checker/VERSION)

link_checker_sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${link_checker_tag_version}'"
	@mvn versions:set -DnewVersion=${link_checker_tag_version} -DgenerateBackupPoms=false -pl link-checker

link_checker_development_env_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker compose -f $(link_checker_docker_compose_development_file) up -d

link_checker_development_env_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker compose -f $(link_checker_docker_compose_development_file) down

link_checker_app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Application"
	@mvn clean package -DskipTests -pl link-checker -am
	@mkdir -p link-checker/target/app/log
	@mkdir -p link-checker/target/app/tmp
	@cp link-checker/target/link-checker.jar link-checker/target/app/service.jar

link_checker_container_production_build: link_checker_app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(link_checker_container_name):$(link_checker_tag_version)"
	@docker build -t $(link_checker_container_name):$(link_checker_tag_version) -t $(link_checker_container_name):latest link-checker/

link_checker_container_production_push: link_checker_container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(link_checker_container_name):$(link_checker_tag_version)"
	@docker push $(link_checker_container_name):$(link_checker_tag_version)
	@docker push $(link_checker_container_name):latest