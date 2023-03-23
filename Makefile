
all: pre-deploy deploy

pre-deploy:
	@kubectl apply -f pre-deploy.yaml

deploy: pre-deploy
	@helm install -f idorg.yaml idorg-matomo bitnami/matomo

upgrade: deploy
	@helm upgrade -f idorg.yaml idorg-matomo bitnami/matomo

uninstall: deploy
	@helm uninstall idorg-matomo
