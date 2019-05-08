import pandas as pd
import re

from halo import Halo

from classes.location import Location
from tools.api import prepare_resource


# Returns a set with all the variants of locations coming from the old dataset.
def create_bad_locations_pool(namespaces):
    spinner = Halo(text='Creating a pool of locations from the old dataset...', spinner='dots')
    spinner.start()

    bad_locations_pool = set()

    for namespace in namespaces:
        for resource in namespace['resources']:
            if ('location' in resource):
                bad_locations_pool.add(resource['location'])

    spinner.succeed(f'Got {len(bad_locations_pool)} bad locations.')

    return bad_locations_pool


# Selects rows from a data frame by a given column and a list of values.
def select_rows(df, col, values):
    return df.loc[df[col].str.lower().isin(values)][['countryCode', 'countryName']].to_dict(orient='records')


# Maps an old location to a new, shiny ISO-3166 country code.
def map_location(old_location, countries):
    result = []

    # First, lets convert everything to lowercase and delete punctuation (except spaces and slash).
    lower_old_location = old_location.lower()
    alpha_regex = re.compile('[^a-z,/ ]')
    clean_old_location = alpha_regex.sub('', lower_old_location)

    # If our old location has slashes or 'and', we split it.
    split_regex = re.compile(',|/| and ')
    locations = split_regex.split(clean_old_location)

    # Strip whitespaces of the split locations.
    locations = [location.strip() for location in locations]

    # Fixes for fringe cases:
    if ('swizerland' in locations):
        locations.append('switzerland')

    if (any(x in ['uk', 'wales'] for x in locations)):
        locations.append('united kingdom')

    if (any('republic of china' in location for location in locations)):
        locations.append('china')

    if (any(x in ['holland', 'the netherlands'] for x in locations)):
        locations.append('netherlands')

    if ('eu' in locations):
        locations.append('belgium')



    result = (
        # First, we attempt to match 2 character country codes.
        result + select_rows(countries, 'countryCode', locations)

        # Then, we attempt to match 3 character country codes.
        + select_rows(countries, 'Alpha-3 code', locations) \

        # after that, we attempt to match country name.
        + select_rows(countries, 'countryName', locations)
    )

    # if (len(result) == 0):
    #     print('EMPTY MAPPING! -> ', end='')
    # print(f'[{old_location}] ==> {result}')

    return result


def populate_locations(countries, destination_url):
    spinner = Halo(spinner='dots')
    spinner.info(f'Posting {len(countries.index)} locations from ISO-3166:"')

    for index, country in countries.iterrows():
        newLocation = Location(country['countryCode'], country['countryName'])

        _ = prepare_resource('locations',
                             newLocation.countryCode,
                             'findByCountryCode',
                             newLocation.serialize(),
                             destination_url)
