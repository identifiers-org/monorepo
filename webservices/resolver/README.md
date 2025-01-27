# Overview
This Web Service implements [__identifiers.org__](http://identifiers.org) Compact ID resolution API for programmatic access to _ID resolution services_.


# Running the Compact ID Resolution service
The only requirement for running the _resolution service_ is having a working installation of [Docker](http://docker.com).

The docker image is called **identifiersorg/cloud-ws-resolver**, and it is part of [identifiers.org Docker Hub](https://hub.docker.com/r/identifiersorg/).

The following command is an example on how to launch the _resolution service_

> docker-compose -f docker-compose-standalone.yml up


# How to Query the Compact ID Resolution Service
There is a Java based library, [libapi](https://github.com/identifiers-org/cloud-libapi), that implements a client for this Web
Service.

Please, refer to its documentation on how to connect to [identifiers.org](https://identifiers.org) Web Services.


# Contributions
Documentation for developers will be coming soon, through the repository Wiki.


### Contact
Manuel Bernal Llinares
