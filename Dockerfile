# This Docker file defines a production container for the Registry API Service at HQ
FROM identifiersorg/linux-java8
LABEL maintainer="Manuel Bernal Llinares <mbdebian@gmail.com>"

# Environment - defaults
ENV HQ_WS_REGISTRY_JVM_MEMORY_MAX 1024m
