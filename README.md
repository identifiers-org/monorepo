# Overview
This Web Service implements [__identifiers.org__](http://identifiers.org) Compact ID resolution API for programmatic access to _ID resolution services_.

# Running the Compact ID Resolution service
TODO

# How to Query the Compact ID Resolution Service
Given a _Compact ID_, e.g. _CHEBI:36927_, and being _resolver_ the name or IP address of the host that's running the service, there are two different endpoints that can be accessed, via __HTTP GET__ requests, at the service.

> http://resolver:8080/Compact_ID

For resolving *Compact_ID* to all the resources that are registered within [identifiers.org](http://identifiers.org) as capable of providing information on that _ID_. The response data comes back in JSON format, e.g. for _CHEBI:36927_, the response looks like

```json
{
    "errorMsg": null,
    "resolvedResources": [
        {
            "accessUrl": "http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:36927",
            "info": "ChEBI (Chemical Entities of Biological Interest)",
            "institution": "European Bioinformatics Institute, Hinxton, Cambridge",
            "location": "UK"
        },
        {
            "accessUrl": "http://www.ebi.ac.uk/ols/ontologies/chebi/terms?obo_id=CHEBI:36927",
            "info": "ChEBI through OLS",
            "institution": "European Bioinformatics Institute, Hinxton, Cambridge",
            "location": "UK"
        },
        {
            "accessUrl": "http://purl.bioontology.org/ontology/CHEBI/CHEBI:36927",
            "info": "ChEBI through BioPortal",
            "institution": "National Center for Biomedical Ontology, Stanford",
            "location": "USA"
        }
    ]
}
```
