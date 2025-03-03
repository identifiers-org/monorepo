# This Docker file defines a production container for the Link Checking Service
FROM amazoncorretto:17-alpine
LABEL maintainer="Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>"

# Environment - defaults
ENV VALIDATOR_JVM_MEMORY_MAX=768m
ENV VALIDATOR_CONFIG_REDIS_HOST=redis
ENV VALIDATOR_CONFIG_BACKEND_SERVICE_RESOLVER_HOST=resolver

# Prepare the application folder
RUN mkdir -p /home/app

# Add the application structure
ADD target/app/. /home/app

# Launch information
EXPOSE 8084
WORKDIR /home/app
#CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
CMD java -Xmx${VALIDATOR_JVM_MEMORY_MAX} -jar service.jar
