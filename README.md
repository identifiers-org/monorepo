# Overview
This Web Service implements [__identifiers.org__](http://identifiers.org) Compact ID resolution API for programmatic access to _ID resolution services_.

Given a _Compact ID_, e.g. _CHEBI:36927_, and being _resolver_ the name or IP address of the host that's running the service, there are two different endpoints that can be accessed, via __HTTP GET__ requests, at the service.

> http://resolver:8080/Compact_ID

For resolving *Compact_ID* to all the resources that are registered within [identifiers.org](http://identifiers.org) as capable of providing information on that _ID_.
