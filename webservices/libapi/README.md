# Overview
This is a Java library that implements clients for [identifiers.org](https://identifiers.org) Web Services.

The following sections will explain how to use the different service wrappers to access
[identifiers.org](https://identifiers.org) Web Services on any of its cloud deployments.

> [!IMPORTANT]
> This library won't be updated on maven central as it had basically zero downloads for a while.
> If you wish to use it, please let us know and we can find a solution for you.
> It is currently a way to share functionality between the web services.


# How to link this library in your code
This library is available at Maven Central, you can use it by just adding the following dependency:

**Maven**
```xml
<dependency>
    <groupId>org.identifiers.cloud</groupId>
    <artifactId>libapi</artifactId>
    <version>1.1.0</version>
</dependency>
```

**Apache Buildr**
```
'org.identifiers.cloud:libapi:jar:1.1.0'
```

**Apache Ivy**
```xml
<dependency org="org.identifiers.cloud" name="libapi" rev="1.1.0" />
```

**Groovy Grape**
```groovy
@Grapes(
@Grab(group='org.identifiers.cloud', module='libapi', version='1.1.0')
)
```

**Gradle/Grails**
```gradle
compile 'org.identifiers.cloud:libapi:1.1.0'
```

**Scala SBT**
```scala
libraryDependencies += "org.identifiers.cloud" % "libapi" % "1.1.0"
```

**Leiningen**
```
[org.identifiers.cloud/libapi "1.1.0"]
```

# Using to [identifiers.org](https://identifiers.org) API Web Services
## Compact ID Resolution Service
The following code snippet shows how to get an instance of the service wrapper for
[identifiers.org](https://identifiers.org) _Resolver Service_, and query the service for a given Compact ID.
```java
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;

// For accessing a locally deployed Resolver service at 'localhost:8080', and requesting resolution of
// Compact ID 'CHEBI:36927'
ServiceResponse<ResponseResolvePayload> response = ApiServicesFactory
                .getResolverService("localhost", "8080")
                .requestCompactIdResolution("CHEBI:36927");

// For accessing a locally deployed Resolver service at 'localhost:8080', and requesting resolution of
// Compact ID 'CHEBI:36927', but constraining the resource provider to 'ebi'
ServiceResponse<ResponseResolvePayload> response = ApiServicesFactory
                .getResolverService("localhost", "8080")
                .requestCompactIdResolution("CHEBI:36927", "ebi");
```

Additional factory methods are available where only the service 'host' is specified, in that case port '80' will be
used, or where no 'host' or 'port' information is given, so the factory will provide an instance of the service client
pointing to any of the [identifiers.org](https://identifiers.org) cloud deployments.


## Metadata Service
The following code snippet shows how to get an instance of the service wrapper for
[identifiers.org](https://identifiers.org) _Metadata Service_, and submit metadata requests.
```java
import org.identifiers.cloud.libapi.services.ApiServicesFactory;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.metadata.ServiceResponseFetchMetadata;
import org.identifiers.cloud.commons.messages.responses.metadata.ServiceResponseFetchMetadataForUrl;

// Requesting metadata for a given Compact ID, the Metadata Service will choose the resource provider with the highest
// recommendation index / score.
ServiceResponse<ResponseFetchMetadataPayload> response = ApiServicesFactory
                .getMetadataService("localhost", "8082")
                .getMetadataForCompactId("CHEBI:36927");

// Requesting metadata for a given URL.
ServiceResponse<ResponseFetchMetadataForUrlPayload> response = ApiServicesFactory
                .getMetadataService("localhost", "8082")
                .getMetadataForUrl("http://reactome.org/content/detail/R-HSA-201451");
```

Additional factory methods are available where only the service 'host' is specified, in that case port '80' will be
used, or where no 'host' or 'port' information is given, so the factory will provide an instance of the service client
pointing to any of the [identifiers.org](https://identifiers.org) cloud deployments.


## Registry Service
The following code snippet shows how to get an instance of the service wrapper for
[identifiers.org](https://identifiers.org) _Registry Service_, and submit registration or validation requests.
```java
import org.identifiers.cloud.commons.messages.models.Requester;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.responses.registry.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;


// Preparing a prefix registration request payload
ServiceRequestRegisterPrefixPayload payload =
            new ServiceRequestRegisterPrefixPayload()
                                    .setName("A Name for this prefix Registration Request")
                                    .setDescription("This is a sample prefix registration request from a unit test of libapi, " +
                                            "we need enouch characters for the description")
                                    .setHomePage("http://your_home.page")
                                    .setOrganization("Your Organization")
                                    .setPreferredPrefix("mynewprefix")
                                    .setResourceAccessRule("http://whatever_url/{$id}")
                                    .setExampleIdentifier("a_sample_id")
                                    .setRegexPattern("\\d+")
                                    .setReferences(new String[]{"ref1", "ref2"})
                                    .setAdditionalInformation("Additional information about this unit test")
                                    .setRequester(new Requester()
                                            .setEmail("requester@your_organization.mail")
                                            .setName("Requester Full Name"));

// Prefix registration request
ServiceResponse<ServiceResponseRegisterPrefixPayload> response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .requestPrefixRegistration(payload);

// There are methods for individual validation of the prefix registration payload fields, they all use the same
// 'ServiceRequestRegisterPrefixPayload' payload object, filled only with the field that wants to be validated. As an
// example, the following lines of code will validate the 'name' field.
// Fill in the payload with just the 'name' field
ServiceRequestRegisterPrefixPayload payload =
            new ServiceRequestRegisterPrefixPayload()
                                    .setName("A Name for this prefix Registration Request");
// Request validation
ServiceResponse<ServiceResponseRegisterPrefixPayload> response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .requestValidationName(payload);
```

Additional factory methods are available where only the service 'host' is specified, in that case port '80' will be
used, or where no 'host' or 'port' information is given, so the factory will provide an instance of the service client
pointing to any of the [identifiers.org](https://identifiers.org) cloud deployments.

## Resolution Data Insight API
It is possible to get 'insights' into currently available resolution data through this API.

The currently supported use cases are shown below this line.

```java
// To get sample URLs to all the resource providers available in the resolution service, the following call can be used
ServiceResponseResolve response = ApiServicesFactory
        .getResolverService("localhost", "8080")
        .getAllSampleIdsResolved();

// To get a home URL for all the resource providers available in the resolution service, the following call can be used
ServiceResponseResolve response = ApiServicesFactory
        .getResolverService("localhost", "8080")
        .getAllHomeUrls();
```


## Responses from the services
The responses from the different services will provide information on how the request was completed, via HTTP Status
code and a possible error message, as well as a specialized payload for the particular request submitted.

For further details, please refer to the javadoc accompanying this library.

# Library Configuration
This library is able to provide clients for the different clouds where [identifiers.org](https://identifiers.org) has
deployed its services, i.e. Amazon Web Services, Google Cloud or Microsoft Azure. By default, a deployment is chosen
randomly between all the possible ones every time a web service client is requested, but this behaviour can be modified
for those use cases where we would like to lock in a cloud provider, i.e. we would like to use [identifiers.org](https://identifiers.org)
web services that are part of only one cloud deployment, this can be done using a _deployment selector_ within the
library configuration as shown in the following code snippet.

```java
import org.identifiers.cloud.libapi.Configuration;

// This call we'll make the library always select identifiers.org AWS deployment
Configuration.selectDeployment(Configuration.InfrastructureDeploymentSelector.AWS);

// To make the library select random deployments again (default behaviour), we use the 'ANY' selector
Configuration.selectDeployment(Configuration.InfrastructureDeploymentSelector.ANY);
```

**Please, take into account that all code samples provided, were pointing to local deployments of [identifiers.org](https://identifiers.org), and it is advised to let the library configuration manager to either choose a random deployment, or lock it down to a particular cloud, so no information regarding _host_ (e.g. 'localhost' in the examples) and _port_ (e.g. 8080 in the case of the resolution code snippet) is needed.**


### Contact
Manuel Bernal Llinares
