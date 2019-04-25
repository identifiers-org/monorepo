import pandas as pd

from halo import Halo


# Returns a set with all the variants of a field coming from the old dataset.
def create_bad_pool(namespaces, what):
    spinner = Halo(text=f'Creating a pool of {what} from the old dataset...', spinner='dots')
    spinner.start()

    bad_pool = set()

    for namespace in namespaces:
        for resource in namespace['resources']:
            if (what in resource):
                bad_pool.add(resource[what])

    spinner.succeed(f'Got {len(bad_pool)} bad {what}.')

    return bad_pool


# Selects rows from a data frame by a given column and a list of values.
def select_rows(df, col, selected_cols, values):
    return df.loc[df[col].str.lower().isin(values)][selected_cols].to_dict(orient='records')