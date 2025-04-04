# This Docker file defines a production container for the Registry API Service at HQ

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

FROM amazoncorretto:17-alpine
LABEL maintainer="Renato Caminha Juacaba neto <rjuacaba@ebi.ac.uk>"

RUN apk --no-cache update && apk --no-cache add ca-certificates
COPY --from=godaddy-certs /godaddy/godaddy-* /usr/local/share/ca-certificates
RUN update-ca-certificates

# Environment - defaults
ENV HQ_WS_REGISTRY_JVM_MEMORY_MAX=1024m

# Prepare the application folder
RUN mkdir -p /home/app

# Add the application structure
ADD target/app/. /home/app

# Launch information
EXPOSE 8180
WORKDIR /home/app
#CMD ["java", "-Xmx1024m", "-jar", "service.jar"]
CMD java -Xmx${HQ_WS_REGISTRY_JVM_MEMORY_MAX} -jar service.jar
