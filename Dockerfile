# This Docker file defines a production container for the Resource Recommender Web Service
FROM identifiersorg/linux-java8
LABEL maintainer="Manuel Bernal Llinares <mbdebian@gmail.com>"

# Prepare the application folder
RUN mkdir -p /home/app

# Add the application structure
ADD target/app/. /home/app

# Launch information
EXPOSE 8083
WORKDIR /home/app
CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
