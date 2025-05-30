### This docker file creates a matomo image based on the bitnami one 
# with the addition of the redis php module for usage with the 
# matomo tracking request queue plugin

# Must be updated when matomo is updated
ARG MATOMO_VERSION="5.3.1"

FROM bitnami/matomo:${MATOMO_VERSION} AS base

# Check latest version at https://pecl.php.net/package/redis
ARG PHP_REDIS_VERSION="6.2.0"



FROM base AS builder
USER root
WORKDIR /php-redis-build

# See https://docs.bitnami.com/aws/apps/dokuwiki/configuration/install-modules-php/#redis
RUN apt-get update && \
    apt-get install -y autoconf build-essential
RUN curl -q -O https://pecl.php.net/get/redis-${PHP_REDIS_VERSION}.tgz
RUN tar xfz redis-${PHP_REDIS_VERSION}.tgz && \
    cd redis-${PHP_REDIS_VERSION} && \
    phpize && \
    ./configure && \
    make &&\
    make install



FROM base AS runner
COPY --from=builder --chown=root:root \
     /opt/bitnami/php/lib/php/extensions/redis.so \
     /opt/bitnami/php/lib/php/extensions/
RUN echo 'extension=redis.so' >> '/opt/bitnami/php/etc/php.ini'
