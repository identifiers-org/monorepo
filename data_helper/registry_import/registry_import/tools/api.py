import json
import requests
import sys
import urllib

from halo import Halo

# Fetch old data from identifiers.org.
def fetch_old_data(originURL):
    spinner = Halo(text='Fetching namespace data from identifiers.org...', spinner='dots')
    spinner.start()

    try:
        response = requests.get(originURL)

        if response.status_code is 200:
            namespaces = response.json()
            spinner.succeed(f'Found {len(namespaces)} namespaces.')
        else:
            spinner.fail(f'Error {response.status_code} fetching namespaces: {response.reason}')
            exit(1)
    except:
        spinner.fail(f'Error fetching namespaces.')
        exit(1)

    return namespaces

# Load old data from a file.
def load_old_data(data_file_name):
    spinner = Halo(text=f'Loading namespace data from {data_file_name}...', spinner='dots')
    spinner.start()

    with open(data_file_name) as source_file:
        namespaces = json.load(source_file)
        spinner.succeed(f'Loaded {len(namespaces)} namespaces.')
        return namespaces


# Get a resource from the rest service.
def do_get(where, what, by, destination_url):
    spinner = Halo(text=f'└─ Getting [{where}] \"{what}\"...', spinner='dots')

    what_param = urllib.parse.quote(what)

    response = requests.get(f"{destination_url}{where}/search/{by}?{by[6].lower() + by[7:]}={what_param}")

    # Converts 404 responses to empty resources.
    if (response.status_code == 404):
        response = None
    else:
        response = response.json()

    if response is not None:
        spinner.succeed(spinner.text[:-3] + ' → [FOUND]')
    else:
        spinner.fail(spinner.text[:-3] + ' → [NOT FOUND]')

    return response


# Post a payload on the rest service.
def do_post(payload, what, where, skip_existing, destination_url):
    spinner = Halo(text=f'└─ Posting [{where}] \"{what}\"...', spinner='dots')

    response = requests.post(f'{destination_url}{where}', json=payload)

    if (response.status_code == 403):
        spinner.fail(spinner.text[:-3] + ' → [FORBIDDEN]')
        exit(1)

    if (response.status_code < 200 or response.status_code > 299):
        if skip_existing and response.status_code == 409 and "duplicate key value violates unique constraint" in response.text:
            spinner.fail(spinner.text[:-3] + ' → [ALREADY EXISTS]')
            return

    response_json = json.loads(response.text)

    spinner.succeed(spinner.text[:-3] + f' → [{response.status_code}]: \"{response_json["_links"]["self"]["href"]}')

    return response_json


# Attempts to fetch resource, and post if it does not exist.
def prepare_resource(where, what, by, payload, destination_url):
    # First try to get the resource.
    entity = do_get(where, what, by, destination_url)

    # If it does not exist, post it.
    if entity is None:
        entity = do_post(payload, what, where, False, destination_url)

    # Extracts reference to the location.
    return entity['_links']['self']['href']
