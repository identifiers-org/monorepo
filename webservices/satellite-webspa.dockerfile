# This file is meant to build the satellite-spa docker image.
#   It needs to be in the webservices root to be able to run
#   using the the maven multi module setup.

FROM node:22-alpine AS react_builder
WORKDIR /home/app
COPY satellite-webspa/site/. .
RUN npm install
RUN npm run build



FROM maven:3-amazoncorretto-17-alpine AS  mvn_builder
WORKDIR /home/app
COPY . .
COPY --from=react_builder /home/app/dist/. /home/app/satellite-webspa/src/main/resources/static/
RUN --mount=type=cache,target=/root/.m2 mvn clean package -pl satellite-webspa -am -DskipTests



FROM amazoncorretto:17-alpine AS runner
LABEL maintainer="Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>"

WORKDIR /home/app

# Environment - defaults
ENV WEB_SATELLITE_WS_JVM_MEMORY_MAX=512m

# Prepare the application folder
RUN mkdir log tmp

# Add the application structure
COPY --from=mvn_builder /home/app/satellite-webspa/target/satellite-webspa.jar ./service.jar

# Launch information
EXPOSE 9091
#CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
CMD java -Xmx${WEB_SATELLITE_WS_JVM_MEMORY_MAX} -jar service.jar
