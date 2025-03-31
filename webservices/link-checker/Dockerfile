FROM alpine/openssl AS godaddy-certs
WORKDIR /godaddy
RUN <<EOR
  wget -q https://certs.godaddy.com/repository/all_intermediate_ca_certificates.p7b
  openssl pkcs7 -in all_intermediate_ca_certificates.p7b -inform DER -print_certs -out godaddy.pem
  awk 'BEGIN {n=0;seen_cert=0}
       split_after==1                {n++;split_after=0}
       /^-----END CERTIFICATE-----$/ {split_after=1;seen_cert=1}
       seen_cert==1                  {print > "godaddy-" n}
       ' godaddy.pem
EOR


# This Docker file defines a production container for the Link Checking Service
FROM amazoncorretto:17-alpine
LABEL maintainer="Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>"

RUN apk --no-cache update && apk --no-cache add ca-certificates
COPY --from=godaddy-certs /godaddy/godaddy-* /usr/local/share/ca-certificates
RUN update-ca-certificates

# Environment - defaults
ENV WS_LINK_CHECKER_JVM_MEMORY_MAX=768m
ENV WS_LINK_CHECKER_CONFIG_REDIS_HOST=redis
ENV WS_LINK_CHECKER_CONFIG_BACKEND_SERVICE_RESOLVER_HOST=resolver

# Prepare the application folder
RUN mkdir -p /home/app

# Add the application structure
ADD target/app/. /home/app

# Launch information
EXPOSE 8084
WORKDIR /home/app
#CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
CMD java -Xmx${WS_LINK_CHECKER_JVM_MEMORY_MAX} -jar service.jar
