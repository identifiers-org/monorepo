# This Makefile helps on building the production container image
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>

# Environment
container_name = identifiersorg/cloud-hq-web-frontend
docker_compose_development_file = docker-compose-development.yml
springboot_development_profile = development
tag_version = $(shell cat VERSION)
dev_site_root_folder = site
file_template_site_index = $(dev_site_root_folder)/src/index.html.template
file_template_environment_site_index = $(dev_site_root_folder)/src/index.template.environment
file_instance_site_index = $(dev_site_root_folder)/src/index.html
development_url_registry_service = http://127.0.0.1:8180
folder_site_dist = site/dist
file_instance_app_structure_index = $(folder_site_dist)/index.html
environment_content = $(shell cat $(file_template_environment_site_index))

# Default target
all: deploy

release: deploy
	@echo "<===|DEVOPS|===> [RELEASE] New Software Release, and next development version prepared"

sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${tag_version}'"
	@docker run -v $(shell pwd)/${dev_site_root_folder}:/home/site node /bin/bash -c "npm --prefix /home/site version ${tag_version}"

deploy: clean container_production_push
	@echo "<===|DEVOPS|===> [DEPLOY] Deploying service container version ${tag_version}"

development_instantiate_index_template: instantiate_base_index_template
	@echo "<===|DEVOPS|===> [DEVELOPMENT] Prepare index template environment"
	@sed -i "s@<!--ENVIRONMENT_PLACEHOLDER-->@${environment_content}@g" ${file_instance_site_index}
	@echo "<===|DEVOPS|===> [DEVELOPMENT] Set development environment"
	@sed -i "s@ENVCONFIG_HQ_WEB_REGISTRY_CONFIG_API_REGISTRY_URL@${development_url_registry_service}@g" ${file_instance_site_index}

force_npm_reinstall:
	@echo "<===|DEVOPS|===> [CLEAN] Deleting npm updated flag"
	@rm npm_install

npm_install:
	@echo "<===|DEVOPS|===> [DEVELOPMENT] Installing npm modules"
	@docker run --user node --network=hqwebnet -p 8192:8192 -v $(shell pwd)/${dev_site_root_folder}:/home/site -it node /bin/bash -c "npm --prefix /home/site install"
	@touch npm_install

development_env_up: development_instantiate_index_template development_env_backend_up npm_install
	@echo "<===|DEVOPS|===> [DEVELOPMENT] Launch development environment"
	@docker run --user node --network=hqwebnet -p 8182:8182 -v $(shell pwd)/${dev_site_root_folder}:/home/site -it node /bin/bash -c "npm --prefix /home/site start"

development_env_down: development_env_backend_down

development_env_backend_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing backend UP"
	@docker-compose -f $(docker_compose_development_file) up -d
	@# TODO Clean this way of referencing the target name in future iterations
	@rm -f development_env_backend_down
	@touch development_env_backend_up

development_env_backend_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing backend DOWN"
	@docker-compose -f $(docker_compose_development_file) down
	@# TODO Clean this way of referencing the target name in future iterations
	@rm -f development_env_backend_up
	@touch development_env_backend_down

development_run_tests: development_env_up
	@echo "<===|DEVOPS|===> [TESTS] Running Unit Tests"
	@mvn -Dspring.profiles.active=$(springboot_development_profile) clean test

instantiate_base_index_template:
	@echo "<===|DEVOPS|===> [PRODUCTION] Prepare base index template"
	@cp ${file_template_site_index} ${file_instance_site_index}

app_structure: instantiate_base_index_template
	@echo "<===|DEVOPS|===> [PACKAGE] Building application structure"
	@docker run -v $(shell pwd)/${dev_site_root_folder}:/home/site node /bin/bash -c "npm --prefix /home/site install; npm --prefix /home/site run build"
	@sed -i "s@<!--ENVIRONMENT_PLACEHOLDER-->@${environment_content}@g" ${file_instance_app_structure_index}

container_production_build: app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(container_name):$(tag_version)"
	@docker build -t $(container_name):$(tag_version) -t $(container_name):latest .

container_production_push: container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(container_name):$(tag_version)"
	@docker push $(container_name):$(tag_version)
	@docker push $(container_name):latest

dev_container_build: clean container_production_build
	@echo "<===|DEVOPS|===> [DEV] Preparing local container"

clean:
	@echo "<===|DEVOPS|===> [CLEAN] Housekeeping"
	@rm -rf ${folder_site_dist}
	@rm -rf ${dev_site_root_folder}/node_modules

.PHONY: all clean app_structure container_production_build development_env_up development_env_down container_production_push dev_container_build deploy release sync_project_version set_next_development_version instantiate_index_template instantiate_base_index_template force_npm_install
