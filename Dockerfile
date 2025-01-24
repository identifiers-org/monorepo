
FROM amazoncorretto:17-alpine
LABEL maintainer="Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>"

# Prepare the application folder
RUN mkdir -p /home/app

# Prepare chrome driver - last tested version: 131.0.6778.108-r0
#  Version is left unset because chromedriver likes to update a lot and apk's versioning is limited
RUN apk add --no-cache --quiet chromium-chromedriver
ENV WS_METADATA_CONFIG_BACKEND_SELENIUM_DRIVER_CHROME_PATH_BIN=/usr/bin/chromedriver

# Add the application structure
ADD target/app/. /home/app

# Environment - defaults
ENV WS_METADATA_JVM_MEMORY_MAX=1024m
ENV WS_METADATA_CONFIG_REDIS_HOST=redis

# Launch information
EXPOSE 8082
WORKDIR /home/app
CMD java -Xmx${WS_METADATA_JVM_MEMORY_MAX} -jar service.jar
