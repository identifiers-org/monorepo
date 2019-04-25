#!/usr/bin/env python3

import configparser
import os
import requests
import sys
import urllib

from data_fixer import create_bad_pool, create_institution_pool, map_location, map_institution

from tools import fetch_old_data, prepare_resource, do_post
from tools import init_config, init_args, load_countries

from classes import Institution, Location, Namespace, Provider, Requester

from halo import Halo


# Inits config and argument parser.
dirname = os.path.dirname(__file__)
config = init_config(os.path.join(dirname, 'config.ini'))
args = init_args()
destination_url = config.get('DestinationURL')
EMPTY_FIELD_LITERAL = config.get('EmptyFieldLiteral')

# Fetches data from old identifiers.org.
namespaces = fetch_old_data(config.get('DataOriginURL'))

# Gets country list.
countries = load_countries(os.path.join(dirname, '../data/ISO-3166.csv'))


# Main loop:
for index, namespace in enumerate(namespaces):
    spinner = Halo(spinner='dots')
    spinner.info(f'Working on namespace [{index}]: \"{namespace["name"]}\"')

    newNamespace = Namespace(prefix=namespace['prefix'].strip(),
                             mirId=namespace['id'].strip(),
                             name=namespace['name'].strip(),
                             pattern=namespace['pattern'].strip(),
                             description=namespace['definition'].strip(),
                             sampleId=namespace['resources'][0]['localId'].strip())

    # Post the namespace.
    namespaceRef = prepare_resource('namespaces',
                                    namespace['prefix'],
                                    'findByPrefix',
                                    newNamespace.serialize(),
                                    destination_url)

    # Post providers for that namespace.
    for provider in namespace['resources']:

        # Post location for that provider.
        if provider.get('location') is not None:
            location = map_location(provider['location'].strip(), countries)[0]

            locationRef = prepare_resource('locations',
                                           location['countryCode'],
                                           'findByCountryCode',
                                           location,
                                           destination_url)

        # Post institution for that resource.
        if provider.get('institution') is not None:
            newInstitution = Institution(name=provider['institution'].strip(),
                                         homeUrl=EMPTY_FIELD_LITERAL,
                                         description=EMPTY_FIELD_LITERAL,
                                         location=locationRef)

            institutionRef = prepare_resource('institutions',
                                              provider['institution'].strip(),
                                              'findByName',
                                              newInstitution.serialize(),
                                              destination_url)

        # Post the provider.
        newProvider = Provider(mirId=provider.get('id').strip(),
                               urlPattern=provider.get('accessURL', EMPTY_FIELD_LITERAL).strip(),
                               name=provider.get('info', EMPTY_FIELD_LITERAL).strip(),
                               description=provider.get('info', EMPTY_FIELD_LITERAL).strip(),
                               official=provider.get('official', 'false'),
                               providerCode=provider.get('resourcePrefix', EMPTY_FIELD_LITERAL).strip(),
                               sampleId=provider.get('localId', EMPTY_FIELD_LITERAL).strip(),
                               resourceHomeUrl=provider.get('resourceURL', EMPTY_FIELD_LITERAL).strip(),
                               location=locationRef,
                               institution=institutionRef,
                               namespace=namespaceRef)

        do_post(newProvider.serialize(), newProvider.name, 'resources', True, config.get('DestinationURL'))

    spinner = Halo(spinner='dots', text_color='green')
    spinner.succeed(f'Namespace [{index}]: \"{newNamespace.name}\" → [SUCCESS]')
    print()
