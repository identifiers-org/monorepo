# Makefile based helper for the Metadata Service

#														#
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>	#
#														#

# Container name
container_name = identifiersorg/cloud-ws-metadata
docker_compose_development_file = docker-compose-development.yml
springboot_development_profile = development

# TODO default target

clean:
	@echo "<===|DEVOPS|===> [CLEAN] Running House Keeping tasks"
	@mvn clean > /dev/null

development_env_up:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker-compose -f $(docker_compose_development_file) up -d
	@# TODO Clean this way of referencing the target name in future iterations
	@rm -f development_env_down
	@touch development_env_up
