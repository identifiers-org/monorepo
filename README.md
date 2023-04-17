
# Matomo setup for identifiers.org

This repository keeps files used to deploy matomo for identifiers.org. 
It may not be kept up to date on current setup since it is easier to manage matomo through its own interface.




## Pre deploy

This deployment setup assumes that the matomo instance will be deployed on Google cloud kubernetes.
Some setup objects aside from the bitnami matomo helm chart are necessary.
These objects are defined in the pre-deploy.yaml file.
This file must be updated and applied before deploying matomo using helm.

No namespace is enforced in this file, but it is assumed that this runs on an isolated namespace on the kubernetes cluster.



### Database

This setup assumes an external database which must be MySQL or MariaDB due to the current Matomo limitations.
Follow this [matomo page](https://matomo.org/faq/how-to-install/faq_23484/) when setting up a user.
Remember to set up the user's permitted hostname accordingly.
Then, update the idorg.yaml and pre-deploy.yaml files with the database access information.

### SSL certificate

This setup uses a google-managed certificate.
Beware it can take up to 24h for the certificate to become active.

### SMTP

To enable Matomo emails, you need to provide the access information to an SMTP server.
Update the secret object on the pre-deploy.yaml file with the external SMTP server password and 
update the idorg.yaml file with the other access information.





## Deployment

Deployment is done with helm and the matomo chart from bitnami. 
Make sure you update the nil (i.e. ~) values from the idorg.yaml and pre-deploy.yaml files.
The pre-deploy.yaml file must be applied to the kubernetes cluster before deployment.
The Makefile contains the rules necessary for the deployments and upgrades.

After the helm deployment, you won't need use helm anymore since matomo allows for upgrades through its web interface.
If you do upgrade or redeploy, you will likely need to redo some "after deployment steps".




## After deployment

- You should change the password for the user after you log in. The initial password is "changethis". 
- The SMTP password doesn't seem to be imported correctly into the matomo config file. You should make sure it is correct via the matomo web interface.
- You need to set up the geoip database for geolocation reporting, the matomo web interface has the instructions for it in the settings. You might need to manually download the database on the misc folder. All the instructions are in the geolocation settings page.
- If you want, you can add the favicon and logo on the branding settings
- When you go into settings, you will see some warnings on the system check
  - You should follow the instructions for the cron setup, the deployment already comes with a cron.d entry, just make sure it is to your liking.
  - You don't need to do the force_ssl option if the cluster already does the ssl change on the frontend as the yaml files for this are setup.
  - If there are files missing in the misc folder, you can just download the matomo src from their website and cherry pick the files you need. This is completely optional.
- You will need to blacklist the IPs from the cluster network otherwise you will have duplicated resolution entries (from web frontend and from resolver api). You can do this by adding the CIDR ranges from the Google cloud default network on the general blacklist on the matomo web interface.
- If you have issues with the persistent file system permissions have a look at [this](https://forum.matomo.org/t/correct-file-permissions/25359) forum post.
