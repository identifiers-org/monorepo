import { fetchAndAdd } from '../utils/fetchAndAdd';

import { config } from '../config/Config';

import { setNamespaceListParams } from './NamespaceListParams';


//
// namespaceList actions.
//

// Get namespaces from registry. Will dispatch setNamespaceList.
export const getNamespacesFromRegistry = (params) => {
  return async (dispatch) => {
    let requestUrl;

    // TODO: Refactor to strategy.
    if (params.content !== '') {
      requestUrl = new URL(`${config.registryApi}/restApi/namespaces/search/findByPrefixContaining`);
      requestUrl.searchParams.append('content', params.content);
    } else if (params.prefixStart !== '') {
      // TODO: What to do with '#' prefixstart?
      requestUrl = new URL(`${config.registryApi}/restApi/namespaces/search/findByPrefixStartsWith`);
      requestUrl.searchParams.append('prefixStart', params.prefixStart);
    } else {
      requestUrl = new URL(`${config.registryApi}/restApi/namespaces`);
    }

    // Params to add to request: PAGE, SIZE, SORT
    requestUrl.searchParams.append('page', params.number);
    requestUrl.searchParams.append('size', params.size);
    requestUrl.searchParams.append('sort', params.sort);

    try {
      const response = await fetch(requestUrl);
      const data = await response.json();
      dispatch(setNamespaceList(data._embedded.namespaces));
      dispatch(setNamespaceListParams({...params, ...data.page}));

      return response;
    }
    catch (err) {
      console.log('Error fetching namespaces: ', err);
    }
  };
};


// Get single namespace from registry. Will dispatch setNamespaces.
export const getNamespaceFromRegistry = (prefix) => {
  return async (dispatch) => {
    let requestUrl = new URL(`${config.registryApi}/restApi/namespaces/search/findByPrefix?prefix=${prefix}`);

    try {
      const response = await fetch(requestUrl);
      const data = await response.json();
      dispatch(setNamespaceList([data]));

      return response;
    }
    catch (err) {
      console.log('Error fetching namespace', err);
    }
  };
};


// Get resources from registry. Will dispatch setResources.
export const getResourcesFromRegistry = (namespace) => {
  return async (dispatch) => {
    let requestUrl = new URL(`${config.registryApi}/restApi/resources/search/findAllByNamespaceId`);
    requestUrl.searchParams.append('id', namespace._links.self.href.split('/').pop());

    try {
      const response = await fetch(requestUrl);
      const data = await response.json();
      const resources = await Promise.all(data._embedded.resources.map(resource => {
        let { _links, ...newResource } = resource;

        return fetchAndAdd(newResource, [
          {name: 'institution', url: _links.institution.href},
          {name: 'location', url: _links.location.href}
        ]);
      }));

      dispatch(setResources(namespace.prefix, resources));
    }
    catch (err) {
      console.log('Error fetching namespaces: ', err);
    };
  };
};


// Redux store update for namespace list.
export const setNamespaceList = (namespaceList) => {
  return {
    type: 'SET_NAMESPACELIST',
    namespaceList
  };
};

export const setResources = (prefix, resources) => {
  return {
    type: 'SET_RESOURCES',
    prefix,
    resources
  }
}