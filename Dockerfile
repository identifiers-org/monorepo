# This Docker file defines a production container for the Satellite WEB SPA
FROM identifiersorg/linux-java8
LABEL maintainer="Manuel Bernal Llinares <mbdebian@gmail.com>"

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
