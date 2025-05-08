SPARQL server for identifiers.org.

The code available here is based on the code from https://github.com/JervenBolleman/sparql-identifiers and 
[Eclipse RDF4j's spring-boot-sparql-web](https://github.com/eclipse-rdf4j/rdf4j/tree/2a998c3f30ec177ecbd3d5bbb4ad209f0c83e2a9/spring-components/spring-boot-sparql-web).

This has three main functionalities:
- Serve registry data (see https://github.com/identifiers-org/ontop/)
- Convert URIs based on URL patterns via a virtual triple store
- Connect identifiers.org URIs to their respective namespace URI

More information can be found at https://docs.identifiers.org/pages/sparql.html
