#!/usr/bin/env python3

import requests
import re
from tqdm.auto import tqdm
import argparse



def find_by_lui(lui, namespace_list):
    luipatterns = {n['prefix']: n['pattern'] for n in namespace_list.values()}
    matching_namespaces = [k for k, p in luipatterns.items() if re.search(p, lui)]
    print("Found", len(matching_namespaces), "matching namespaces")
    found_namespaces = list()
    for n in tqdm(matching_namespaces):
        for nr in namespaces[n]['resources']:
            url = nr['urlPattern'].replace('{$id}', lui)
            try:
                with requests.head(url, timeout=3.05) as resp:
                    if 200 <= resp.status_code < 300:
                        found_namespaces.append([n, url])
            except Exception:
                pass
    return found_namespaces


def find_by_url(url_part, namespace_list):
    urlpatterns = {f"Resource {r['id']} of namespace {n['prefix']}": r['urlPattern'] for n in namespace_list.values() for r in n['resources']}
    return [prefix for prefix, pattern in tqdm(urlpatterns.items()) if re.search(url_part, pattern)]



if __name__ == '__main__':
    parser = argparse.ArgumentParser(prog='Namespace searcher',
        description='Looks for namespace either by url or by lui',
        epilog='Not the best')

    parser.add_argument(dest='search_method', choices=['url', 'lui'],
                        help='Either to search via URL or via LUI patterns')
    parser.add_argument(dest='search_string',  type=str,
                        help='The string to search using the method (URL component or LUI)')
    parser.add_argument('--resolver-url', required=False, dest='resolver_url',
                        default="https://registry.api.identifiers.org/resolutionApi/getResolverDataset")
    args = parser.parse_args()

    headers = {'Accept': 'application/json'}
    with requests.get(args.resolver_url, headers) as response:
        data = response.json()
    namespaces = {n['prefix']: n for n in data['payload']['namespaces']}

    print("Searching", args.search_string, "by", args.search_method)
    results = []
    if args.search_method == 'lui':
        results = find_by_lui(args.search_string, namespaces)
    elif args.search_method == 'url':
        results = find_by_url(args.search_string, namespaces)

    print(" ", len(results), "matches where found")
    for r in results:
        if type(r) == list:
            print('  ->', *r)
        else:
            print('  ->', r)
