# This Makefile helps on building the production container image

# These are meant to be run from the parent folder

webspa_container_name = identifiersorg/cloud-satellite-web-spa
webspa_docker_compose_development_file = satellite-webspa/docker-compose-development.yml
springboot_development_profile = development
webspa_tag_version = $(shell cat satellite-webspa/VERSION)

webspa_dev_site_root_folder = satellite-webspa/site
webspa_folder_site_dist = satellite-webspa/site/dist
webspa_folder_spring_boot_static = satellite-webspa/src/main/resources/static
webspa_network_docker_name = satwebspanet

webspa_sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${webspa_tag_version}'"
	@docker run -v $(shell pwd)/${webspa_dev_site_root_folder}:/home/site node /bin/bash -c "npm --prefix /home/site version ${webspa_tag_version}"
	@mvn versions:set -DnewVersion=${webspa_tag_version} -pl satellite-webspa -DgenerateBackupPoms=false

#npm_install:
#	@echo "<===|DEVOPS|===> [DEVELOPMENT] Installing npm modules"
#	@docker run --user node --network=$(webspa_network_docker_name) -p 9091:9091 -p 50001:50001 -v $(shell pwd)/${webspa_dev_site_root_folder}:/home/site -it node /bin/bash -c "npm --prefix /home/site install; npm rebuild"

webspa_development_env_up: webspa_development_env_backend_up webspa_app_structure
	@echo "<===|DEVOPS|===> [DEVELOPMENT] Launch development environment"
	@docker run --user node --network=$(webspa_network_docker_name) -p 9091:9091 -p 50001:50001 -v $(shell pwd)/${webspa_dev_site_root_folder}:/home/site -it node /bin/bash -c "npm --prefix /home/site start"

webspa_development_env_down: webspa_development_env_backend_down

webspa_development_env_backend_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing backend UP"
	@docker compose -f $(webspa_docker_compose_development_file) up -d

webspa_development_env_backend_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing backend DOWN"
	@docker compose -f $(webspa_docker_compose_development_file) down

webspa_app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Building SPA structure"
	@docker run --rm -v $(shell pwd)/${webspa_dev_site_root_folder}:/home/site node:22-alpine sh -c "npm --prefix /home/site install; npm --prefix /home/site run build"
	@echo "<===|DEVOPS|===> [PACKAGE] Copying SPA tree to Spring Boot static content folder"
	@mkdir -p $(webspa_folder_spring_boot_static)
	@cp -R $(webspa_folder_site_dist)/* $(webspa_folder_spring_boot_static)/.

webspa_container_production_build: webspa_clean_local_static_files
	@echo "<===|DEVOPS|===> [BUILD] Production container $(webspa_container_name):$(webspa_tag_version)"
	@docker build -f satellite-webspa.dockerfile -t $(webspa_container_name):$(webspa_tag_version) -t $(webspa_container_name):latest .

webspa_container_production_push: webspa_container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(webspa_container_name):$(webspa_tag_version)"
	@docker push $(webspa_container_name):$(webspa_tag_version)
	@docker push $(webspa_container_name):latest

webspa_clean_local_static_files:
	@echo "<===|DEVOPS|===> [CLEAN]> Cleaning static files in $(webspa_folder_spring_boot_static) and $(webspa_folder_site_dist)/*"
	@rm -f $(webspa_folder_spring_boot_static)/* $(webspa_folder_site_dist)/*