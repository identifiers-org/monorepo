# This Docker file defines a production container for the Registry API Service at HQ
FROM amazoncorretto:17-alpine
LABEL maintainer="Renato Caminha Juacaba neto <rjuacaba@ebi.ac.uk>"

# Environment - defaults
ENV HQ_WS_REGISTRY_JVM_MEMORY_MAX 1024m

# Prepare the application folder
RUN mkdir -p /home/app

# Add the application structure
ADD target/app/. /home/app

# Launch information
EXPOSE 8180
WORKDIR /home/app
#CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
CMD java -Xmx${HQ_WS_REGISTRY_JVM_MEMORY_MAX} -jar service.jar
