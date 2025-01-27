# Makefile Helper for setting up and run the Data Helper
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>
all:
	@echo "<===|DEVOPS|===> [ALL] There is no default target for this helper"

install_requirements:
	@echo "<===|DEVOPS|===> [INSTALL] Installing Application Requirements"
	@pip3 install --user pipenv
	@PIPENV_VENV_IN_PROJECT=1 pipenv install

tmp:
	@echo "<===|DEVOPS|===> [MKDIR] Temporary folder"
	@mkdir tmp

dev_environment: install_requirements
	@echo "<===|DEVOPS|===> [INSTALL] Development Environment"

install: dev_environment
	@echo "<===|DEVOPS|===> [INSTALL] Initializing Application Installation"

update_requirements_file: dev_environment
	@echo "<===|DEVOPS|===> [UPDATE] Application Requirements"
	@pipenv lock -r

upgrade_dependencies:
	@echo "<===|DEVOPS|===> [UPDATE] Application Requirements Upgrade"
	@pipenv update

import_registry:
	@echo "<===|DEVOPS|===> [IMPORT] Importing HQ Registry Dataset"
	@pipenv run registry_import/registry_import/populate_registry.py -f registry_import/data/identifiers.json

import_registry_local:
	@echo "<===|DEVOPS|===> [IMPORT] Importing HQ Registry Dataset with MIR ID loading"
	@pipenv run registry_import/registry_import/populate_registry.py -f registry_import/data/identifiers.json -z

import_registry_authenabled_local:
	@echo "<===|DEVOPS|===> [IMPORT] Authenticating and importing HQ Registry Dataset with MIR ID loading"
	@pipenv run registry_import/registry_import/populate_registry.py -f registry_import/data/identifiers.json -z -a test test


clean_dev:
	@echo "<===|DEVOPS|===> [CLEAN] Removing Python Virtual Environment"
	@pipenv --rm

clean_logs:
	@echo "<===|DEVOPS|===> [CLEAN] Removing logs"
	@rm -rf logs/*log

clean_tmp:
	@echo "<===|DEVOPS|===> [CLEAN] Removing Temporary folder"
	@rm -rf tmp

clean: clean_logs clean_tmp
	@echo "<===|DEVOPS|===> [CLEAN] Housekeeping"

clean_all: clean clean_dev
	@echo "<===|DEVOPS|===> [CLEAN] Housekeeping, clean all"

.PHONY: install dev_environment install_requirements update_requirements_file upgrade_dependencies clean_logs clean_dev clean_all clean_tmp clean import_registry import_registry_local import_registry_authenabled_local
