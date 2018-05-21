# This Makefile helps on building the production container image, and with the release cycle
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>	#
# Some "facts"
container_name = identifiersorg/cloud-ws-link-checker
docker_compose_development_file = docker-compose-development.yml
springboot_development_profile = development
tag_version = $(shell cat VERSION)
