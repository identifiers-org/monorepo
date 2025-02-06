# Makefile based helper for the Metadata Service

# These are meant to be run from the parent folder

# Google Chrome Driver
version_latest_chrome_driver = 132.0.6834.160
url_base_chrome_driver = https://storage.googleapis.com/chrome-for-testing-public/
binary_linux_chromedriver = chromedriver-linux64
binary_mac_chromedriver = chromedriver-mac-arm64
binary_windows_chromedriver = chromedriver-win64
url_download_linux_chromedriver = $(url_base_chrome_driver)$(version_latest_chrome_driver)/linux64/$(binary_linux_chromedriver).zip
url_download_mac_chromedriver = $(url_base_chrome_driver)$(version_latest_chrome_driver)/mac-arm64/$(binary_mac_chromedriver).zip
url_download_windows_chromedriver = $(url_base_chrome_driver)$(version_latest_chrome_driver)/win64/$(binary_windows_chromedriver).zip
# Development support
metadata_container_name = identifiersorg/cloud-ws-metadata
metadata_docker_compose_development_file = metadata/docker-compose-development.yml
springboot_development_profile = metadata/development
metadata_tag_version = $(shell cat metadata/VERSION)

metadata_sync_project_version:
	@echo "<===|DEVOPS|===> [SYNC] Synchronizing project version to version '${metadata_tag_version}'"
	@mvn versions:set -DnewVersion=${metadata_tag_version} -pl metadata

metadata_development_env_up: chromedriver
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment UP"
	@docker-compose -f $(metadata_docker_compose_development_file) up -d

metadata_development_env_down:
	@echo "<===|DEVOPS|===> [ENVIRONMENT] Bringing development environment DOWN"
	@docker-compose -f $(metadata_docker_compose_development_file) down

metadata_app_structure:
	@echo "<===|DEVOPS|===> [PACKAGE] Application"
	@mvn clean package -DskipTests -pl metadata -am
	@mkdir -p metadata/target/app/log
	@mkdir -p metadata/target/app/tmp
	@cp metadata/target/metadata.jar metadata/target/app/service.jar

metadata_container_production_build: metadata_app_structure
	@echo "<===|DEVOPS|===> [BUILD] Production container $(metadata_container_name):$(metadata_tag_version)"
	@docker build -t $(metadata_container_name):$(metadata_tag_version) -t $(metadata_container_name):latest metadata/

metadata_container_production_push: metadata_container_production_build
	@echo "<===|DEVOPS|===> [PUBLISH]> Production container $(metadata_container_name):$(metadata_tag_version)"
	@docker push $(metadata_container_name):$(metadata_tag_version)
	@docker push $(metadata_container_name):latest

chromedriver: metadata/tmp metadata/bin/selenium
	@echo "<===|DEVOPS|===> [CHROME DRIVERS] Ensuring drivers exist"
	@cd metadata/tmp; if [ ! -e ../bin/selenium/chromedriver-linux ]; then \
		wget $(url_download_linux_chromedriver); \
		unzip $(binary_linux_chromedriver).zip; mv $(binary_linux_chromedriver)/chromedriver ../bin/selenium/chromedriver-linux; fi
	@cd metadata/tmp; if [ ! -e ../bin/selenium/chromedriver-mac ]; then \
		wget $(url_download_mac_chromedriver); \
		unzip $(binary_mac_chromedriver).zip; mv $(binary_mac_chromedriver)/chromedriver ../bin/selenium/chromedriver-mac; fi
	@cd metadata/tmp; if [ ! -e ../bin/selenium/chromedriver.exe ]; then \
		wget $(url_download_windows_chromedriver); \
		unzip $(binary_windows_chromedriver).zip; mv $(binary_windows_chromedriver)/chromedriver.exe ../bin/selenium/; fi

metadata/tmp:
	@echo "<===|DEVOPS|===> [FOLDER] Preparing temporary folder"
	@mkdir metadata/tmp

metadata/bin/selenium:
	@echo "<===|DEVOPS|===> [FOLDER] Preparing selenium folder for binaries"
	@mkdir -p metadata/bin/selenium