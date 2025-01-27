
all: pre-deploy deploy
include .env
pre-deploy:
	@kubectl ${K8S_NAMESPACE} apply -f pre-deploy.yaml

deploy: pre-deploy
	@helm ${K8S_NAMESPACE} install -f idorg.yaml idorg-matomo bitnami/matomo

diff:
	@helm ${K8S_NAMESPACE} diff upgrade -f idorg.yaml idorg-matomo bitnami/matomo #Requires diff plugin for helm

upgrade:
	@helm ${K8S_NAMESPACE} upgrade -f idorg.yaml idorg-matomo bitnami/matomo

uninstall: deploy
	@helm ${K8S_NAMESPACE} uninstall idorg-matomo
