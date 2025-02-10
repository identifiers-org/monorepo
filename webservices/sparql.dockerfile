# This Docker file defines a production container for the Sparql Web Service
FROM maven:3-amazoncorretto-17-alpine AS builder

WORKDIR /home/app
COPY . .

RUN --mount=type=cache,target=/root/.m2 mvn clean package -pl sparql -am -DskipTests



FROM amazoncorretto:17-alpine AS runner
LABEL maintainer="Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>"

# Environment - defaults
ENV WS_SPARQL_JVM_MEMORY_MAX=1024m

# Prepare the application folder
RUN mkdir -p /home/app

# Add the application structure
COPY --from=builder "/home/app/sparql/target/sparql.jar" "/home/app/service.jar"

# Launch information
EXPOSE 8080
WORKDIR /home/app
#CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
CMD java -Xmx${WS_SPARQL_JVM_MEMORY_MAX} -jar service.jar
