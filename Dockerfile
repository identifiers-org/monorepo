# HQ Web fronted container definition
FROM nginx
LABEL maintainer="Manuel Bernal Llinares <mbdebian@gmail.com>"

# Configure the default site
COPY nginx_conf/site_default.conf /etc/nginx/conf.d/default.conf
# Prepare startup Application
RUN mkdir -p /home/app
# Copy startup script
COPY scripts/startup.sh /home/app/startup.sh
RUN chmod 750 /home/app/startup.sh
# Prepare site
RUN mkdir -p /home/site
ADD site/. /home/site
RUN chown nginx:nginx -R /home/site

# Publish the following ports
EXPOSE 8080

# Working directory
WORKDIR /home/app

# Launch container
CMD ./startup.sh
