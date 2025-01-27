from halo import Halo

import more_itertools as mit

import asyncio
from aiohttp import ClientSession


# Find ranges of unused mirids in a mirid array.
def find_empty_ranges(miriam_array):
    start = miriam_array[0]
    end = miriam_array[-1]
    current_last = start

    ranges = []

    for group in mit.consecutive_groups(miriam_array):
        group = list(group)

        empty_group = current_last + 1, group[0] - 1
        current_last = group[-1]

        if empty_group[0] < empty_group[1]:
            ranges.append(empty_group)

    return ranges


# Async request
async def fetch(url, session):
    async with session.get(url) as response:
        return await response.read()


# Async semaphore fetch
async def bound_fetch(sem, url, session):
    async with sem:
        await fetch(url, session)


async def async_mint(first_miriam, last_miriam, url):
    mir_requests = []
    sem = asyncio.Semaphore(200)

    async with ClientSession() as session:
        for mir_id in range(first_miriam, last_miriam):
            load_mirid_url = f'{url}MIR:{str(mir_id).zfill(8)}'

            task = asyncio.ensure_future(bound_fetch(sem, load_mirid_url, session))
            mir_requests.append(task)

        await asyncio.gather(*mir_requests)


# Mint MIR ids in the new database. It will get data from a txt file with a list of
# all the mirids from the old database, one per line, sorted from smallest to biggest.
# That file is data/mirids.txt
#
# After minting all ids in the interval between 0 and the last one in the file, it will
# return the ids that are not present in that file (the gaps).
def mint_used_ids(mirids_file_name, mirid_controller_url):
    spinner = Halo(spinner='dots')
    spinner.info('Minting used mirids from old database in the new one...')

    with open(mirids_file_name) as mirids_file:
        lines = mirids_file.read().splitlines()

    miriam_array = []

    for mirid in lines:
        if len(mirid) != 12:
            spinner.fail(f'Invalid miriam: [{mirid}]')

        else:
            miriam_array.append(int(mirid.split(':')[1]))

    first_miriam = miriam_array[0]
    last_miriam = miriam_array[-1]

    spinner.info(f'Found [{len(miriam_array)}] MIR ids, spanning from [{first_miriam}] to [{last_miriam}]...')

    spinner.start(f'Launching {last_miriam} async load requests...')

    loop = asyncio.get_event_loop()
    future = asyncio.ensure_future(async_mint(first_miriam, last_miriam, f'{mirid_controller_url}loadId/'))
    loop.run_until_complete(future)

    spinner.succeed(f'Done.')


    spinner = Halo(spinner='dots')
    spinner.info('Returning mirids in gaps between used ones...')

    unused_miriams = list(find_empty_ranges(miriam_array))

    spinner.info(f'Found [{len(unused_miriams)}] empty MIR id blocks: {unused_miriams}')

    for empty_block in unused_miriams:
        spinner.info(f'Returning MIR ids in range [{empty_block}]')

        return_miriam_array = []

        for mir_id in range(empty_block[0], empty_block[1]):
            return_miriam_array.append(mir_id)

        spinner.start(f'Launching {len(return_miriam_array)} async return requests...')

        future = asyncio.ensure_future(async_mint(return_miriam_array[0], return_miriam_array[-1], f'{mirid_controller_url}returnId/'))
        loop.run_until_complete(future)

        spinner.succeed(f'Done!')
