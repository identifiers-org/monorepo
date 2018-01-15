# This Docker file defines a production container for the Resolver Web Service
FROM identifiersorg/linux-java8
MAINTAINER Manuel Bernal Llinares <mbdebian@gmail.com>

# Prepare the application folder
RUN mkdir -p /home/app

# Add the application structure
ADD target/app/. /home/app

# Launch information
EXPOSE 8080
WORKDIR /home/app
CMD ["java", "-jar", "service.jar"]
