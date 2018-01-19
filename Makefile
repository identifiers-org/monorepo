# This Makefile helps on building the production container image

# Default target
all: clean container_production_push

app_structure:
	@echo "---> Creating application structure"
	@mvn clean > /dev/null
	@mvn package
	@mkdir -p target/app/log
	@mkdir -p target/app/tmp
	@cp target/resolver-*.jar target/app/service.jar

container_production_build: app_structure
	@echo "---> Building production container"
	@docker build -t identifiersorg/cloud-ws-resolver .

container_production_push: container_production_build
	@docker push identifiersorg/cloud-ws-resolver

dev_container_build: container_production_build

clean:
	@echo "---> Cleaning the space"
	@mvn clean > /dev/null

.PHONY: all clean app_structure container_production_build container_production_push dev_container_build
