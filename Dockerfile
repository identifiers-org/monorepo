# This Dockerfile defines how to containerize this service.
FROM identifiersorg/linux-java8
LABEL maintainer="Manuel Bernal Llinares <mbdebian@gmail.com>"

# Environment - defaults
ENV HQ_WS_MIR_ID_CONTROLLER_JVM_MEMORY_MAX 768m
