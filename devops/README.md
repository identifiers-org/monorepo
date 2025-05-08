These are helm charts for identifiers.org  

These can be used deploy the respective microservices with the correct settings and versions on a kubernetes cluster. 
Please refer to the `Makefile` under each folder for the helm commands normally used for deployment.
Please beware that these require files that aren't stored in this repository for various reasons.
In the worst case, you can reverse engineer this file using helm diff upgrade if you have access to the deployments.

Identifiers.org services are split into three charts: matomo, satellite, and HQ. 

The **matomo** setup uses the bitnami chart v0.2.15 and encapsulates our matomo instance and its recurring archiver job.

The **Satellite** chart groups the resolution services of identifiers.org, in addition to the metadata resolver and the SPARQL server.
This deployment can be replicated by users in case they wish to perform resolution within their premises.
Feel free to contact us for help in this regard. 
Also, it is important that you inform us if you perform resolution locally so we can consider you on our updates.

The **HQ** chart contains the registry and its closer orbiting components, such as the authentication server and SQL database.
This deployment is meant to be the source of truth for identifiers.org and its resolvers. 
*We do not recommend users to replicate this deployment* and instead use our main registry.


## New version deployment process
1. Generate new version of container image & push to docker hub (see respective webservice Makefile) with appropriate version tag
2. Update the appropriate values.yaml file with the new version
3. Use diff command (see chart Makefile) to check if changes are what are expected
4. Use upgrade command to apply changes to deployment


> [!Caution]
> For developers: [beware of Google Cloud Ingress limitations](https://cloud.google.com/kubernetes-engine/docs/concepts/ingress#limitations)
> when updating container health checks. 

These changes do not propagate automatically to the Google cloud backend health checks.
This causes containers to be in a healthy state, according to their readiness and liveness probes, 
but not healthy according to google cloud backend service health check. These are different entities.
The backend service is initially instantiated by the Google cloud ingress implementation, but not updated afterward.