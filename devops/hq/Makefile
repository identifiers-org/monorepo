include .env

install:
	@helm ${K8S_NAMESPACE} install -f production.yaml ${DEPLOYMENT_NAME} .

diff: #Requires diff plugin for helm, see diff-install target
	@helm ${K8S_NAMESPACE} diff upgrade -f production.yaml ${DEPLOYMENT_NAME} .

upgrade:
	@helm ${K8S_NAMESPACE} upgrade -f production.yaml ${DEPLOYMENT_NAME} .

uninstall:
	@helm ${K8S_NAMESPACE} uninstall ${DEPLOYMENT_NAME}

diff-install:
	@helm plugin install https://github.com/databus23/helm-diff
