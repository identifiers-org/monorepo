# This Docker file defines a production container for the Satellite WEB SPA
FROM amazoncorretto:17-alpine
LABEL maintainer="Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>"

# Environment - defaults
ENV WEB_SATELLITE_WS_JVM_MEMORY_MAX 512m

# Prepare the application folder
RUN mkdir -p /home/app

# Add the application structure
ADD target/app/. /home/app

# Launch information
EXPOSE 9091
WORKDIR /home/app
#CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
CMD java -Xmx${WEB_SATELLITE_WS_JVM_MEMORY_MAX} -jar service.jar
