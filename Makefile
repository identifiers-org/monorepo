# This Makefile helps on building the production container image
app-structure:
	@echo -e "---> Creating application structure"
	@mvn clean > /dev/null
	@mvn package
	@mkdir -p target/app/log
	@mkdir -p target/app/tmp
	@cp target/resolver-*.jar target/app/service.jar

.PHONY: app-structure
