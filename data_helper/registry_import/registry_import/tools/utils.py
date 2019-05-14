import argparse
import configparser
import csv
import os

import pandas as pd

from halo import Halo


def init_config(config_file_name):
  spinner = Halo(text='Initializing config', spinner='dots')
  spinner.start()

  # Decides where are we working on.
  env = os.getenv('ENV') or 'DEV'

  # Fetches config.
  config = configparser.ConfigParser()
  config.read_file(open(config_file_name))

  spinner.info(f'Using {env} environment.')

  return config[env]


def init_args():
  # Argument parser.
  parser = argparse.ArgumentParser(description='Populates repositories of Identifiers.org cloud.')
  parser.add_argument('-s', '--skiperror', action='store_true', help='Continue on error.')
  parser.add_argument('-v', '--verbose', action='store_true', help='Show detailed output.')
  parser.add_argument('-m', '--miriam', action='store_true', help='Also populate miriams in database.')
  parser.add_argument('-n', '--skipnamespaces', action='store_true', help='Skip namespace population (use along -m).')
  parser.add_argument('-l', '--skiplocations', action='store_true', help='Skip location population.')
  return parser.parse_args()


def load_countries(country_csv_file_name):
  spinner = Halo(text='Loading ISO-3166 country list...', spinner='dots')
  spinner.start()

  # Load countries from ISO-3166 listing.
  countries = pd.read_csv(country_csv_file_name,
                             sep=',',
                             header=0,
                             quotechar='"',
                             skipinitialspace = True,
                             names=['countryName', 'countryCode', 'Alpha-3 code'],
                             usecols=[0, 1, 2],
                             keep_default_na=False)

  spinner.succeed(f'Got {len(countries)} countries.')

  return countries






  # # Prepare csv reader and skip header.
  #   csv_reader = csv.reader(country_csv, delimiter=',', quotechar='"')
  #   next(csv_reader, None)

  #   country_list = {rows[1]:rows[0] for rows in csv_reader}