# This Makefile helps on building the production container image
app-structure:
	@echo -e "---> Creating application structure"
	@mvn clean > /dev/null
	@mvn package
	@mkdir -p target/app/log
	@mkdir -p target/app/tmp
	@cp target/resolver-*.jar target/app/service.jar

production-container: app-structure
	@docker build -t identifiersorg/cloud-ws-resolver
	@docker push identifiersorg/cloud-ws-resolver

clean:
	@mvn clean > /dev/null

.PHONY: app-structure production-container clean
