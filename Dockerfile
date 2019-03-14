# This Dockerfile defines the container for this service
FROM identifiersorg/linux-java8
LABEL maintainer="Manuel Bernal Llinares <mbdebian@gmail.com>"

# Environment - defaults
ENV HQ_WEB_FRONTEND_JVM_MEMORY_MAX 1024m
