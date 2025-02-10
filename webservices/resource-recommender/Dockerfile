# This Docker file defines a production container for the Resource Recommender Web Service
FROM amazoncorretto:17-alpine
LABEL maintainer="Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>"

# Environment - defaults
ENV WS_RESOURCE_RECOMMENDER_JVM_MEMORY_MAX=768m

# Prepare the application folder
RUN mkdir -p /app

# Add the application structure
ADD target/app/. /app

# Launch information
EXPOSE 8083
WORKDIR /app
#CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
CMD java -Xmx${WS_RESOURCE_RECOMMENDER_JVM_MEMORY_MAX} -jar service.jar
