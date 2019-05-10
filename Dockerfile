# HQ Web fronted container definition
FROM nginx
LABEL maintainer="Manuel Bernal Llinares <mbdebian@gmail.com>"

# Configure the default site
COPY nginx_conf/site_default.conf /etc/nginx/conf.d/default.conf
# Prepare startup Application
