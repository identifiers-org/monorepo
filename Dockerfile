# This Docker file defines a production container for the Registry API Service at HQ
FROM identifiersorg/linux-java8
LABEL maintainer="Manuel Bernal Llinares <mbdebian@gmail.com>"

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
