#!python3

import requests
import sys
import argparse
import json
import urllib


# #########################################################################
# Parameters
namespace_origin_url = "https://identifiers.org/rest/collections/expand"
destination_url = "http://127.0.0.1:8180/restApi/"
# #########################################################################

# Argument parser.
parser = argparse.ArgumentParser(description='Populates repositories of Identifiers.org cloud.')

_ = parser.add_argument('-s', '--skiperror', action='store_true', help='Continue on error.')
_ = parser.add_argument('-v', '--verbose', action='store_true', help='Show detailed output.')

args = parser.parse_args()


# #########################################################################
# Fetch data from old Identifiers.org
print("Fetching namespace data from identifiers.org...")

namespaces = requests.get(namespace_origin_url).json()

print(f"Found {len(namespaces)} namespaces.")
# #########################################################################


# Get a resource from the rest service.
def do_get(where, what, by):
    print(f"  getting \"{what}\" from \"{where}\"...", end=' ')

    response = requests.get(f'{destination_url}{where}/search/{by}?{by[6].lower() + by[7:]}={urllib.parse.quote(what)}')

    # Converts 404 responses to empty resources.
    if (response.status_code == 404):
        response = None
    else:
        response = response.json()

    print (f"FOUND: \"{response['_links']['self']['href']}\"" if response is not None else 'NOT FOUND')

    return response


# Post a payload on the rest service.
def do_post(payload, what, where, skip_existing=False):
    print(f"  Posting payload \"{what}\" in \"{where}\"...", end=' ')

    response = requests.post(f'{destination_url}{where}', json=payload)

    if (response.status_code < 200 or response.status_code > 299):
        if skip_existing and response.status_code == 409 and "duplicate key value violates unique constraint" in response.text:
            print("ALREADY EXISTS")
            return
        if args.verbose is False:
            print(response.text)
        if args.skiperror is False:
            sys.exit(1)

    response_json = json.loads(response.text)

    print(f"{response.status_code}: \"{response_json['_links']['self']['href']}\"" if args.verbose is False else response.text)
    return response_json


# Attempts to fetch resource, and post if it does not exist.
def prepare_resource(where, what, by, payload):
    # First try to get the resource.
    entity = do_get(where, what, by)

    # If it does not exist, post it.
    if entity is None:
        entity = do_post(payload, what, where)

    # Extracts reference to the location.
    return entity['_links']['self']['href']


# Main loop:
for idx, namespace in enumerate(namespaces):
    print(f"* Working on namespace [{idx}]: \"{namespace['name']}\"")

    # First, get/add the namespace.
    namespaceRef = prepare_resource(
        'namespaces',
        namespace['prefix'],
        'findByPrefix',
        {
            'prefix': namespace['prefix'].strip(),
            'mirId': namespace['id'].strip(),
            'name': namespace['name'].strip(),
            'pattern': namespace['pattern'].strip(),
            'description': namespace['definition'][:255].strip(),
            'sampleId': namespace['resources'][0]['localId'].strip()      # TODO missing in old DB: ContactPerson.
        }
    )

    # Add resources for that namespace.
    for resource in namespace['resources']:
        # Get/add locations for that resource.
        if resource.get('location') is not None:
            locationRef = prepare_resource(
                'locations',
                resource['location'].strip(),
                'findByCountryCode',
                {'countryCode': resource['location'].strip(), 'countryName': resource['location'].strip()}
            )

        # Get/add institutions for that resource.
        if resource.get('institution') is not None:
            institutionRef = prepare_resource(
                'institutions',
                resource['institution'].strip(),
                'findByName',
                {
                    'name': resource['institution'].strip(),
                    'description': 'empty',
                    'location': locationRef,
                    'homeUrl': 'http://www.google.com'
                }
            )

        # Add the resource.
        newResource = {
            'mirId': resource.get('id').strip(),
            'urlPattern': resource.get('accessURL', 'empty').strip(),
            'name': resource.get('info', 'empty').strip(),
            'description': resource.get('info', 'empty').strip(),
            'official': resource.get('official', 'false'),
            'providerCode': resource.get('resourcePrefix', 'empty').strip(),
            'sampleId': resource.get('localId', 'empty').strip(),
            'resourceHomeUrl': resource.get('resourceURL', 'empty').strip(),
            'location': locationRef,
            'institution': institutionRef,
            'namespace': namespaceRef
        }

        do_post(newResource, newResource['mirId'], 'resources', skip_existing=True)

        print('Done.', end='\n\n')

