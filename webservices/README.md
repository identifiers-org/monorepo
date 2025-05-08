This folder contains the multi-module maven project used to develop the identifiers.org web services.

Please refer to the various Makefile for the commands used. 
**Beware that the make command is meant to be called from the webservices folder, not it's subfolders**.

## Versioning

Each submodule is versioned independently via the VERSION file on the root folder of each submodule.
To upgrade the version, edit the respective VERSION file and run the appropriate `*_sync_project_version` make target
(for example: `make sparql_sync_project_version`) to propagate it to the associated pom.xml and package.json files.

Please see devops folder on how to release & deploy a new version.


## Dev environment

Each submodule has a docker compose yaml file that brings up the required for that submodule.
Please refer to the make targets `*_development_env_up` on how to use these files 
(for example: `make mirid_development_env_up`).
With the required dev environment up, you can run the submodule spring application directly using maven or your preferred IDE.
Each `*_development_env_up` make target has an associated `*_development_env_down` target to undo the setup.
