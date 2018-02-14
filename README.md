# Overview
This Web Service implements [__identifiers.org__](http://identifiers.org) Compact ID resolution API for programmatic access to _ID resolution services_.


# Running the Compact ID Resolution service
The only requirement for running the _resolution service_ is having a working installation of [Docker](http://docker.com).

The docker image is called **identifiersorg/cloud-ws-resolver**, and it is part of [identifiers.org Docker Hub](https://hub.docker.com/r/identifiersorg/).

The following command is an example on how to launch the _resolution service_

> docker run -p 8080:8080 identifiersorg/cloud-ws-resolver


# How to Query the Compact ID Resolution Service
Given a _Compact ID_, e.g. _CHEBI:36927_, and being _resolver_ the name or IP address of the host that's running the service, there are two different endpoints that can be accessed, via __HTTP GET__ requests, at the service.

> http://resolver:8080/Compact_ID

For resolving *Compact_ID* to all the resources that are registered within [identifiers.org](http://identifiers.org) as capable of providing information on that _ID_. The response data comes back in JSON format, e.g. for _CHEBI:36927_, the response looks like

```json
{
    "errorMessage": null,
    "resolvedResources": [
        {
            "accessUrl": "http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:36927",
            "info": "ChEBI (Chemical Entities of Biological Interest)",
            "institution": "European Bioinformatics Institute, Hinxton, Cambridge",
            "location": "UK",
            "official": true
        },
        {
            "accessUrl": "http://www.ebi.ac.uk/ols/ontologies/chebi/terms?obo_id=CHEBI:36927",
            "info": "ChEBI through OLS",
            "institution": "European Bioinformatics Institute, Hinxton, Cambridge",
            "location": "UK",
            "official": false
        },
        {
            "accessUrl": "http://purl.bioontology.org/ontology/CHEBI/CHEBI:36927",
            "info": "ChEBI through BioPortal",
            "institution": "National Center for Biomedical Ontology, Stanford",
            "location": "USA",
            "official": false
        }
    ]
}
```

In case you're interested in getting only a specific resource from the resolution service, you can use the following endpoint

> http://resolver:8080/selector/Compact_ID

Where _selector_ is used by the resolver for filtering out any other provider than the one specified, e.g. if, for the same _CHEBI:36927_, I wanted the resolver to give me only the EBI resource, I could do it submitting my _HTTP GET_ request like this

> http://resolver:8080/ebi/CHEBI:36927

the response would be

```json
{
    "errorMessage": null,
    "resolvedResources": [
        {
            "accessUrl": "http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:36927",
            "info": "ChEBI (Chemical Entities of Biological Interest)",
            "institution": "European Bioinformatics Institute, Hinxton, Cambridge",
            "location": "UK",
            "official": true
        }
    ]
}
```

Successful requests will return an _HTTP OK_ (200) status, if the provided _Compact ID_ could not be resolved, an _HTTP NOT FOUND_ (404) would be sent back to the client, with some explanation on what happened, e.g. when querying with _Compact ID_ _newprefix:36927_, this is the response

```json
{
    "errorMsg": "No providers found for Compact ID 'newprefix:36927'",
    "resolvedResources": []
}
```

Something similar will happen when using the _selector_ endpoint, if the given resource _selector_ does not exists, even if the _Compact ID_ exists and can be resolved, e.g. for _selector_ _moon_ and the same _Compact ID_ as before

```json
{
    "errorMsg": "No providers found for Compact ID 'CHEBI:36927', selector 'moon'",
    "resolvedResources": []
}
```

In the case of an internal error when attending a request, an _HTTP INTERNAL SERVER ERROR_ would be sent back to the client with, probably, some description of what could have happened.


# Contributions
Documentation for developers will be coming soon, through the repository Wiki.


### Contact
Manuel Bernal Llinares
