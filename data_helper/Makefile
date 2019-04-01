# Makefile Helper for setting up and run the Data Helper
# Author: Manuel Bernal Llinares <mbdebian@gmail.com>
all:
	@echo "<===|DEVOPS|===> [ALL] There is no default target for this helper"

install_requirements:
	@echo "<===|DEVOPS|===> [INSTALL] Installing Application Requirements"
	@python_install/bin/pip install pipreqs nose
	@python_install/bin/pip install -r requirements.txt

python_install:
	@echo "<===|DEVOPS|===> [INSTALL] Python Virtual Environment"
	@pip install --user virtualenv
	@virtualenv python_install

tmp:
	@echo "<===|DEVOPS|===> [MKDIR] Temporary folder"
	@mkdir tmp

dev_environment: python_install install_requirements
	@echo "<===|DEVOPS|===> [INSTALL] Development Environment"

install: dev_environment
	@echo "<===|DEVOPS|===> [INSTALL] Initializing Application Installation"

update_requirements_file: dev_environment
	@echo "<===|DEVOPS|===> [UPDATE] Application Requirements"
	#@python_install/bin/pipreqs --use-local --savepath requirements.txt $(PWD)
	@python_install/bin/pip freeze > requirements.txt

upgrade_dependencies:
	@echo "<===|DEVOPS|===> [UPDATE] Application Requirements Upgrade"
	@pip list --outdated --format=freeze | grep -v '^\-e' | cut -d = -f 1  | xargs -n1 pip install -U
	@echo "<===|DEVOPS|===> [USER_INTERACTION] Please, run an update on the requirements file"

clean_dev:
	@echo "<===|DEVOPS|===> [CLEAN] Removing Python Virtual Environment"
	@rm -rf python_install

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

.PHONY: install dev_environment install_requirements update_requirements_file upgrade_dependencies clean_logs clean_dev clean_all clean_tmp clean
