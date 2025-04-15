# HQ Web frontend container definition
FROM nginx
LABEL maintainer="Renato Juacaba Neto <rjuacaba@gmail.com>"

# Default Environment
ENV SITEMAP_BUILDER_CONFIG_PATH_FILE_SITEMAP=/home/site/sitemap.txt

# Prepare Python environment
RUN apt-get update -y \
    && apt-get install -y --no-install-recommends \
      python3 python3-pip python3-requests \
    && rm -rf /var/lib/apt/lists/*

# Configure the default site
COPY nginx_conf/site_default.conf /etc/nginx/conf.d/default.conf
# Prepare startup Application
RUN mkdir -p /home/app
# Copy startup script
COPY --chmod=750 scripts/startup.sh /home/app/startup.sh
# Copy sitemap updater
COPY --chmod=750 scripts/sitemap_updater.py /home/app/sitemap_updater.py
# Prepare site
RUN mkdir -p /home/site
ADD --chown=nginx:nginx site/dist/. /home/site
ADD --chown=nginx:nginx seo/. /home/site

# Publish the following ports
EXPOSE 80

# Working directory
WORKDIR /home/app

# Getting rid of /bin/sh
# RUN cp /bin/bash /bin/sh

# Launch container
CMD ./startup.sh
