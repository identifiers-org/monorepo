# This Docker file defines a production container for the Resolver Web Service
FROM identifiersorg/linux-java8
LABEL maintainer="Manuel Bernal Llinares <mbdebian@gmail.com>"

# Environment - defaults
ENV WS_RESOLVER_JVM_MEMORY_MAX 1024m

# Prepare the application folder
RUN mkdir -p /home/app

# Add the application structure
ADD target/app/. /home/app

# Launch information
EXPOSE 8080
WORKDIR /home/app
#CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
CMD java -Xmx${WS_RESOLVER_JVM_MEMORY_MAX} -jar service.jar
