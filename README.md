# Overview
This Web Service implements [__identifiers.org__](http://identifiers.org) _Compact ID metadata extraction API_ for programmatic access to _metadata resolution services_.


# Running the Metadata Resolution Service
The only requirement for running the _resolution service_ is having a working installation of [Docker](http://docker.com).

Although this service is part of [__identifiers.org__](http://identifiers.org) **Satellite Deployments**, it can be run in _standalone_ mode, i.e. if you just want to bring this service up for programmatic access to metadata information associated with a given _Compact ID_.

Download, from this repo, the file named [_docker-compose-standalone.yml_](https://raw.githubusercontent.com/identifiers-org/cloud-ws-metadata/master/docker-compose-standalone.yml), and issue the following command:

> docker-compose -f docker-compose-standalone.yml up -d

This will launch the _Metadata Resolution Service_, if you'd like to stop it, just run:

> docker-compose -f docker-compose-standalone.yml down


# How to query the Metadata Resolution Service
To query this _Metadata Resolution Service_, being 'servicehost' the host that's running the service, and given an example ID 'CHEBI:36927', you submit an _HTTP GET_ request like this:

> http://servicehost:8082/CHEBI:36927

For this particular example, the resource providing information on _CHEBI:36927_ _Compact ID_ does not include metadata, so the response body from the service looks like:

```json
{
    "errorMessage": "FAILED to fetch metadata for Compact ID 'CHEBI:36927', because 'JSON-LD formatted METADATA NOT FOUND for URL 'http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:36927', content \n'HtmlHead[<head>]''",
    "metadata": ""
}
```

And, _HTTP Status_ is _400 Bad Request_ in this case, although a _404 NOT FOUND_ would have made more sense, this is the could native prototype version of the service, and its first software lifecycle iteration.

We want to get feedback from the community as soon as possible, so it can be refined once we can better shape the use cases.

Upon successful metadata extraction, an _HTTP Status 200 OK_ is returned back to the client, including the metadata information in the response body.

_When different providers (resources) exist for a given Compact ID, this service implements a simple selection strategy based on which resource is the 'official' one, and that's the resource used for metadata extraction_


# Contributions
Documentation for developers will be coming soon, through the repository Wiki.


### Contact
Manuel Bernal Llinares
