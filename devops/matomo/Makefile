
all: pre-deploy deploy
include .env
pre-deploy:
	@kubectl ${K8S_NAMESPACE} apply -f pre-deploy.yaml

deploy: pre-deploy
	@helm ${K8S_NAMESPACE} install -f idorg.yaml -f production.yaml idorg-matomo 'oci://registry-1.docker.io/bitnamicharts/matomo' --version '0.2.15'

diff: #Requires diff plugin for helm, see diff-install target
	helm ${K8S_NAMESPACE} diff upgrade -f idorg.yaml -f production.yaml idorg-matomo 'oci://registry-1.docker.io/bitnamicharts/matomo' --version '0.2.15' 

upgrade:
	@helm ${K8S_NAMESPACE} upgrade -f idorg.yaml -f production.yaml idorg-matomo 'oci://registry-1.docker.io/bitnamicharts/matomo' --version '0.2.15'

uninstall:
	@helm ${K8S_NAMESPACE} uninstall idorg-matomo --version '0.2.15'

diff-install:
	@helm plugin install https://github.com/databus23/helm-diff