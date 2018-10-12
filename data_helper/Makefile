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
